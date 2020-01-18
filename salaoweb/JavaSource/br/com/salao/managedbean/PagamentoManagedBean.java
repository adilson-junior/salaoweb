package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Pagamento;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Vale;
import br.com.salao.modelo.vo.PagamentoVO;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name = "pagamentoMB")
@ViewScoped
public class PagamentoManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;	
	private List<Profissional> profissionais;
	private List<PagamentoVO> pagamentosVO;
	private List<Pagamento> pagamentos;
	private List<PagamentoVO> pagamentosSelecionadosVO;
	private List<Vale> vales;
	private List<Outros> listOutros;
	private PagamentoVO pagamentoVO;
	private String totalVale;
	private String totalOutros;
	private Date dataInicio;
	private Date dataFinal;
	private String nomeProfissional;
	private Float valorAjuste;	
	private String total;
	
	public PagamentoManagedBean(){			
		pagamentosSelecionadosVO = new ArrayList<PagamentoVO>();
		pagamentoVO = new PagamentoVO();
		setPagamentosVO(new ArrayList<PagamentoVO>());
		pagamentos = new ArrayList<Pagamento>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		vales = new ArrayList<Vale>();
		listOutros = new ArrayList<Outros>();
	}
	
	@PostConstruct
	public void iniciar(){				
	}	
	
	public void listar(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataInicio);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		dataFinal = calendar.getTime();
		try {
			pagamentos = financeiroServico.buscarPagamentos(dataInicio, dataFinal);
			Float total = 0f;
			for(Pagamento pag : pagamentos){
				total += pag.getTotal();
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();
			this.total = nFormat.format(total);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
		}
	}
	
	public void salvarPagamentos(){
		if(pagamentosSelecionadosVO.size() == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Pagamento", "Selecione um ou mais pagamentos");
			return;
		}
		List<Pagamento> pagamentos = new ArrayList<Pagamento>();
		for(PagamentoVO pag : pagamentosSelecionadosVO){
			Pagamento pagamento = new Pagamento();
			pagamento.setData(new Date());
			pagamento.setDe(dataInicio);
			pagamento.setAte(dataFinal);
			pagamento.setAjuste(pag.getAjuste());
			pagamento.setOutros(pag.getOutros());
			pagamento.setVale(pag.getVale());
			pagamento.setValor(pag.getValor());
			Profissional prof = new Profissional();
			prof.setId(pag.getIdProfissional());
			pagamento.setProfissional(prof);
			pagamentos.add(pagamento);
		}
		try {
			financeiroServico.salvarPagamentos(pagamentos);
			calcularPagamentos();
			addMensagem(FacesMessage.SEVERITY_INFO, "Pagamento", "Sucesso");
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
		}
	}
	
	public void incrementarValor(){	
		Float total = pagamentoVO.getValor() 
				- Math.abs(pagamentoVO.getOutros()) 
				- Math.abs(pagamentoVO.getVale()) 
				+ valorAjuste;
		pagamentoVO.setTotal(total);
		pagamentoVO.setAjuste(valorAjuste);
		pagamentoVO = new PagamentoVO();
		valorAjuste = null;
	}
	
	public void decrementarValor(){		
		if(valorAjuste > pagamentoVO.getTotal()){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Pagamento", "O valor do ajuste é maior que o saldo.");
			return;
		}
		Float total = pagamentoVO.getValor() 
				- Math.abs(pagamentoVO.getOutros()) 
				- Math.abs(pagamentoVO.getVale()) 
				- valorAjuste;
		pagamentoVO.setTotal(total);
		if(valorAjuste > 0){
			pagamentoVO.setAjuste(valorAjuste * -1);
		}else{
			pagamentoVO.setAjuste(valorAjuste);
		}
		pagamentoVO = new PagamentoVO();
		valorAjuste = null;
	}
	
	public void cancelarAjuste(){
		pagamentoVO = new PagamentoVO();
		valorAjuste = null;
	}
	
	public void selecionarDetalhesProfissional(){
		Profissional profissional = null;
		for(Profissional prof : profissionais){
			if(pagamentoVO.getIdProfissional().equals(prof.getId())){
				profissional = prof;
				break;
			}
		}
		this.vales.clear();
		SortedSet<Vale> vales = profissional.getVales();		
		Float totalVale = 0F;
		Float totalOutros = 0f;
		for(Vale vl : vales){
			this.vales.add(vl);
			totalVale += vl.getValor();
		}
		this.listOutros.clear();
		SortedSet<Outros> outros = profissional.getOutros();
		for(Outros out : outros){
			this.listOutros.add(out);
			totalOutros += out.getValor();
		}
		NumberFormat nFormat = NumberFormat.getCurrencyInstance();
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
	}
	
	public void calcularPagamentos(){
		try {
			pagamentosVO.clear();
			if(!intervaloValido(dataInicio, dataFinal)){
				addMensagem(FacesMessage.SEVERITY_ERROR, "Pagamento", "Selecione um intervalo no mesmo mês.");			
				return;
			}
			List<Profissional> profissionaisCheck = profissionalServico.listar(true, dataFinal);
			List<Pagamento> pagamentos = financeiroServico.buscarPagamentos(dataInicio, dataFinal);
			for(Pagamento pag : pagamentos){
				profissionaisCheck.remove(pag.getProfissional());
			}	
			if(profissionaisCheck.size() == 0){
				addMensagem(FacesMessage.SEVERITY_INFO, "Pagamento", "Não há pagamentos pendentes.");			
				return;
			}
			List<Integer> ids = new ArrayList<Integer>();
			for(Profissional prof : profissionaisCheck){	
				ids.add(prof.getId());
			}
			profissionais = null;
			profissionais = profissionalServico.buscarProfissionaisEItensFaturamento(dataInicio, dataFinal, ids);				
			for(Profissional prof : profissionais){			
				profissionaisCheck.remove(prof);
				PagamentoVO pag = new PagamentoVO();
				SortedSet<Item> itens = prof.getItens();
				Float totalVale = 0F;				
				Float totalOutros = 0f;
				Float totalComissao = 0f;				
				for(Item it : itens){
					float perc = it.getComissao() / 100;
					float val = ((it.getValor() * it.getQuantidade()) - it.getDesconto());
					totalComissao += perc * val;
				}	
				SortedSet<Vale> vales = prof.getVales();
				for(Vale vl : vales){
					totalVale += vl.getValor();
				}
				SortedSet<Outros> outros = prof.getOutros();
				for(Outros out : outros){
					totalOutros += out.getValor();
				}
				pag.setNomeProfissional(prof.getNome());
				pag.setIdProfissional(prof.getId());				
				pag.setValor(totalComissao);
				if(totalVale > 0){
					pag.setVale(totalVale * -1);
				}else{
					pag.setVale(totalVale);
				}	
				if(totalOutros > 0){
					pag.setOutros(totalOutros * -1);
				}else{
					pag.setOutros(totalOutros);
				}					
				pag.setAjuste(0f);				
				Float total = totalComissao - totalVale - totalOutros;
				pag.setTotal(total);
				pagamentosVO.add(pag);
			}
			for(Profissional prof : profissionaisCheck){					
				PagamentoVO pag = new PagamentoVO();				
				pag.setNomeProfissional(prof.getNome());
				pag.setIdProfissional(prof.getId());				
				pag.setValor(0f);		
				pag.setVale(0f);
				pag.setAjuste(0f);
				pag.setOutros(0f);			
				pag.setTotal(0f);
				pagamentosVO.add(pag);
			}
			Collections.sort(pagamentosVO, new Comparator<PagamentoVO>(){
				@Override
				public int compare(PagamentoVO o1, PagamentoVO o2) {					
					return o1.getNomeProfissional().compareToIgnoreCase(o2.getNomeProfissional());
				}				
			});			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	private boolean intervaloValido(Date inicio, Date fim){
		boolean ok = false;	
		Calendar calendarIni = Calendar.getInstance();
		calendarIni.setTime(inicio);		
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(fim);	
		int mesIni = calendarIni.get(Calendar.MONTH);
		int mesFim = calendarFim.get(Calendar.MONTH);		
		if(mesIni == mesFim){
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

	public List<PagamentoVO> getPagamentosVO() {
		return pagamentosVO;
	}

	public void setPagamentosVO(List<PagamentoVO> pagamentosVO) {
		this.pagamentosVO = pagamentosVO;
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

	public String getTotalOutros() {
		return totalOutros;
	}

	public void setTotalOutros(String totalOutros) {
		this.totalOutros = totalOutros;
	}

	public PagamentoVO getPagamentoVO() {
		return pagamentoVO;
	}

	public void setPagamentoVO(PagamentoVO pagamentoVO) {
		this.pagamentoVO = pagamentoVO;
	}

	public String getNomeProfissional() {
		return nomeProfissional;
	}

	public void setNomeProfissional(String nomeProfissional) {
		this.nomeProfissional = nomeProfissional;
	}

	public Float getValorAjuste() {
		return valorAjuste;
	}

	public void setValorAjuste(Float valorAjuste) {
		this.valorAjuste = valorAjuste;
	}

	public List<PagamentoVO> getPagamentosSelecionadosVO() {
		return pagamentosSelecionadosVO;
	}

	public void setPagamentosSelecionadosVO(List<PagamentoVO> pagamentosSelecionadosVO) {
		this.pagamentosSelecionadosVO = pagamentosSelecionadosVO;
	}

	public List<Outros> getListOutros() {
		return listOutros;
	}

	public void setListOutros(List<Outros> listOutros) {
		this.listOutros = listOutros;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}
