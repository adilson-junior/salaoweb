package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Vale;
import br.com.salao.modelo.vo.ExtratoDetalhadoVO;
import br.com.salao.servico.EntradaSaidaServico;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name = "extratoProfMB")
@ViewScoped
public class ExtratoProfissionalManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(name = "entradaSaidaServico", value = "#{entradaSaidaServico}")
	private EntradaSaidaServico entradaSaidaServico;
	private Profissional profissional;
	private Date dataInicio;
	private Date dataFinal;
	private List<ExtratoDetalhadoVO> extratosDetalhados;
	private List<Profissional> profissionais;	
	private String totalComissao;
	private String tipoExtrato;
	private boolean exibirResumido;
	private boolean exibirDetalhado;
	private List<Vale> vales;
	private List<Outros> listOutros;
	private String totalVale;
	private String totalOutros;
	private Float saldo;
	
	public ExtratoProfissionalManagedBean(){	
		extratosDetalhados = new ArrayList<ExtratoDetalhadoVO>();	
		vales = new ArrayList<Vale>();
		listOutros = new ArrayList<Outros>();
		totalComissao = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		tipoExtrato = "R";
		exibirResumido = true;
		exibirDetalhado = false;
	}
	
	@PostConstruct
	public void iniciar(){			
		listarProfissionais();
		calcularExtrato();
	}
	
	public void calcularExtrato(){
		if(profissional == null || profissional.getId() == null){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato Profissional", "Selecione um profissional");			
		}else{
			if(!intervaloValido(dataInicio, dataFinal, "dia")){
				addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato Profissional", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
				return;
			}
			if(tipoExtrato.equals("R")){
				exibirResumido = true;
				exibirDetalhado = false;
				calcularExtratoResumido();
			}else{
				exibirResumido = false;
				exibirDetalhado = true;
				calcularExtratoDetalhado();
			}
		}		
	}
	
	private void calcularExtratoResumido(){
		try {			
			List<Comanda> comandas = null;
			extratosDetalhados.clear();
			comandas = financeiroServico.buscarComandasProfissional(dataInicio, dataFinal, profissional.getId());		
			vales = entradaSaidaServico.listarVales(profissional.getId(), dataInicio, dataFinal);
			listOutros = entradaSaidaServico.listarOutross(profissional.getId(), dataInicio, dataFinal);
			Float totalVale = 0f;
			Float totalOutros = 0f;
			for(Vale val : vales){
				totalVale += val.getValor();
			}
			for(Outros out : listOutros){
				totalOutros += out.getValor();
			}		
			Float totalComissao2 = 0f;			
			for(Comanda com : comandas){
				String nomeCliente = null;				
				Float totalVenda = 0F;
				Float totalDesconto = 0F;	
				Float totalComissao = 0f;				
				for(Item it : com.getItens()){
					if(nomeCliente == null){
						nomeCliente = it.getCliente().getNome();
					}
					totalVenda += (it.getValor() * it.getQuantidade());
					totalDesconto += it.getDesconto();									
					totalComissao += (it.getComissao() / 100) * ((it.getValor() * it.getQuantidade()) - it.getDesconto());
					totalComissao2 += (it.getComissao() / 100) * ((it.getValor() * it.getQuantidade()) - it.getDesconto());
				}
				ExtratoDetalhadoVO extrato = new ExtratoDetalhadoVO();
				extrato.setData(com.getDataPagamento());
				extrato.setCliente(nomeCliente);
				if(totalDesconto > 0){
					extrato.setDesconto(totalDesconto * -1);
				}else{
					extrato.setDesconto(totalDesconto);
				}
				extrato.setValor(totalVenda);		
				extrato.setTotal(totalVenda - totalDesconto);
				extrato.setComissao(totalComissao);
				extratosDetalhados.add(extrato);					
			}		
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();
			this.totalComissao = nFormat.format(totalComissao2);				
			saldo = totalComissao2 - totalVale - totalOutros;
			if(totalVale > 0){
				this.totalVale = nFormat.format(totalVale * -1);
			}else{
				this.totalVale = nFormat.format(totalVale);
			}
			if(totalOutros > 0){
				this.totalOutros = nFormat.format(totalOutros * -1);
			}else{
				this.totalOutros = nFormat.format(totalOutros);
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato",e.getMessage());
		}
	}
	
	private void calcularExtratoDetalhado(){
		try {			
			extratosDetalhados.clear();
			List<Comanda> comandas = null;
			comandas = financeiroServico.buscarComandasProfissional(dataInicio, dataFinal, profissional.getId());	
			vales = entradaSaidaServico.listarVales(profissional.getId(), dataInicio, dataFinal);
			listOutros = entradaSaidaServico.listarOutross(profissional.getId(), dataInicio, dataFinal);
			Float totalVale = 0f;
			Float totalOutros = 0f;
			for(Vale val : vales){
				totalVale += val.getValor();
			}
			for(Outros out : listOutros){
				totalOutros += out.getValor();
			}
			Float totalComissao = 0f;
			Float total = 0f;
			for(Comanda com : comandas){					
				Float totalVenda = 0F;		
				Float totalValor = 0f;
				Float comissao = 0f;
				for(Item it : com.getItens()){					
					totalVenda = (it.getValor() * it.getQuantidade());								
					totalValor = totalVenda - it.getDesconto();
					total += totalValor;
					ExtratoDetalhadoVO extratoDetalhadoVO = new ExtratoDetalhadoVO();
					extratoDetalhadoVO.setItem(it.getNomeItem());
					extratoDetalhadoVO.setCliente(it.getCliente().getNome());
					extratoDetalhadoVO.setPercentComissao(it.getComissao());
					comissao = (it.getComissao()/100)*totalValor;
					totalComissao += comissao;
					extratoDetalhadoVO.setComissao(comissao);
					if(it.getDesconto() > 0){
						extratoDetalhadoVO.setDesconto(it.getDesconto() * -1);
					}else{
						extratoDetalhadoVO.setDesconto(it.getDesconto());
					}
					extratoDetalhadoVO.setValor(it.getValor());
					extratoDetalhadoVO.setTotal(totalValor);
					extratoDetalhadoVO.setQtd(it.getQuantidade());
					extratoDetalhadoVO.setTipoItem(it.getTipoItem());
					extratoDetalhadoVO.setData(it.getData());
					extratosDetalhados.add(extratoDetalhadoVO);
				}
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();					
			this.totalComissao = nFormat.format(totalComissao);				
			saldo = totalComissao - totalVale  - totalOutros;
			if(totalVale > 0){
				this.totalVale = nFormat.format(totalVale * -1);
			}else{
				this.totalVale = nFormat.format(totalVale);
			}
			if(totalOutros > 0){
				this.totalOutros = nFormat.format(totalOutros * -1);
			}else{
				this.totalOutros = nFormat.format(totalOutros);
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato",e.getMessage());
		}
	}
	
	public List<Profissional> pesquisarProfissional(String chave){ 
		List<Profissional> profis = new ArrayList<Profissional>();
		for(Profissional prof : profissionais){
			if(prof.getNome().toLowerCase().startsWith(chave)){
				profis.add(prof);
			}
		}
		return profis;
	}
	
	public void listarProfissionais(){
		try {
			profissionais = null;
			profissionais = profissionalServico.listar(true);		
			if(profissionais.size() > 0){
				profissional = profissionais.get(0);				
			}
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	private boolean intervaloValido(Date inicio, Date fim, String tipoAgrupamento){
		boolean ok = false;
		int anoMesIni = 0;
		int anoMesFim = 0;
		int intervalo = 0;
		int limite = 0;
		Calendar calendarIni = Calendar.getInstance();
		calendarIni.setTime(inicio);		
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(fim);	
		int mesIni = calendarIni.get(Calendar.MONTH);
		int mesFim = calendarFim.get(Calendar.MONTH);		
		if(tipoAgrupamento.equals("dia")){
			long dif = calendarFim.getTimeInMillis() - calendarIni.getTimeInMillis();	
			anoMesIni = mesIni;
			anoMesFim = mesFim;
			intervalo = (int)(dif / 86400000)+1;
			limite = 30;
		}else{// Mês
			anoMesIni = calendarIni.get(Calendar.YEAR);			
			anoMesFim = calendarFim.get(Calendar.YEAR);
			intervalo = (Math.abs((mesIni + 1) - 13)) + (mesFim + 1);
			limite = 12;
		}		
		if(anoMesIni == anoMesFim){
			ok = true;
		}else if(anoMesIni < anoMesFim && intervalo <= limite){
			ok = true;
		}		
		return ok;
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}	

	public List<ExtratoDetalhadoVO> getExtratosDetalhados() {
		return extratosDetalhados;
	}

	public void setExtratosDetalhados(List<ExtratoDetalhadoVO> extratosDetalhados) {
		this.extratosDetalhados = extratosDetalhados;
	}

	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}	

	public String getTotalComissao() {
		return totalComissao;
	}

	public void setTotalComissao(String totalComissao) {
		this.totalComissao = totalComissao;
	}

	public boolean isExibirResumido() {
		return exibirResumido;
	}

	public void setExibirResumido(boolean exibirResumido) {
		this.exibirResumido = exibirResumido;
	}

	public boolean isExibirDetalhado() {
		return exibirDetalhado;
	}

	public void setExibirDetalhado(boolean exibirDetalhado) {
		this.exibirDetalhado = exibirDetalhado;
	}

	public String getTipoExtrato() {
		return tipoExtrato;
	}

	public void setTipoExtrato(String tipoExtrato) {
		this.tipoExtrato = tipoExtrato;
	}

	public EntradaSaidaServico getEntradaSaidaServico() {
		return entradaSaidaServico;
	}

	public void setEntradaSaidaServico(EntradaSaidaServico entradaSaidaServico) {
		this.entradaSaidaServico = entradaSaidaServico;
	}

	public List<Vale> getVales() {
		return vales;
	}

	public void setVales(List<Vale> vales) {
		this.vales = vales;
	}

	public String getTotalVale() {
		return totalVale;
	}

	public void setTotalVale(String totalVale) {
		this.totalVale = totalVale;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public List<Outros> getListOutros() {
		return listOutros;
	}

	public void setListOutros(List<Outros> listOutros) {
		this.listOutros = listOutros;
	}

	public String getTotalOutros() {
		return totalOutros;
	}

	public void setTotalOutros(String totalOutros) {
		this.totalOutros = totalOutros;
	}
	
}
