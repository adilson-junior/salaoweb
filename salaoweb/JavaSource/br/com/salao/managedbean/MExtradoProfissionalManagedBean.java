package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Vale;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name = "mextratoMB")
@ViewScoped
public class MExtradoProfissionalManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(value="#{mloginMB}")
	private MLoginManagedBean mLoginManagedBean;
	private String totalVale;
	private String totalOutros;	
	private String totalDesconto;
	private String totalComissao;
	private String totalValor;
	private String total;
	private String saldo;
	private Profissional profissional;
	private String periodo;
	private List<Item> itens;
	
	public MExtradoProfissionalManagedBean(){	
		itens = new ArrayList<Item>();
		periodo = "M";
	}
	
	@PostConstruct
	public void iniciar(){	
		profissional = mLoginManagedBean.getProfissional();
	}
	
	public String listarAtendimentosHoje(){
		try {
			itens = financeiroServico.buscarClientesEItensFaturamentosHoje(profissional.getId());
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Saldo", e.getMessage());				
		}
		return "pm:atendimentos_hoje";
	}
	
	public String calcaluarSaldo(){
		Calendar calendar = Calendar.getInstance();
		if(periodo.equals("M")){
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}		
		Date dataInicio = calendar.getTime();
		Date dataFinal = new Date();	
		Profissional profissional;
		try {
			profissional = profissionalServico.buscarProfissionalEItensFaturamento(dataInicio, dataFinal, this.profissional.getId());
			SortedSet<Item> itens = profissional.getItens();
			Float totalVale = 0F;				
			Float totalOutros = 0f;
			Float totalComissao = 0f;	
			Float totalDesconto = 0f;
			Float totalValor = 0f;
			for(Item it : itens){				
				float perc = it.getComissao() / 100;
				float val = it.getValor() * it.getQuantidade();
				totalComissao += perc * (val - it.getDesconto());
				totalValor += val;
				totalDesconto += it.getDesconto();
			}	
			SortedSet<Vale> vales = profissional.getVales();
			for(Vale vl : vales){
				totalVale += vl.getValor();
			}
			SortedSet<Outros> outros = profissional.getOutros();
			for(Outros out : outros){
				totalOutros += out.getValor();
			}	
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();			
			this.totalValor = nFormat.format(totalValor);	
			if(totalDesconto > 0){
				this.totalDesconto = nFormat.format(totalDesconto * -1);
			}else{
				this.totalDesconto = nFormat.format(totalDesconto);
			}
			this.total = nFormat.format(totalValor - totalDesconto);
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
			this.totalComissao = nFormat.format(totalComissao);
			this.saldo = nFormat.format(totalComissao - totalVale - totalOutros);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Saldo", e.getMessage());				
		}		
		return "pm:saldo_mes";
	}
	
	public void calcularSaldoDetalhado(){
		
	}		

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
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

	public String getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(String total) {
		this.totalValor = total;
	}

	public String getTotalComissao() {
		return totalComissao;
	}

	public void setTotalComissao(String totalComissao) {
		this.totalComissao = totalComissao;
	}

	public MLoginManagedBean getmLoginManagedBean() {
		return mLoginManagedBean;
	}

	public void setmLoginManagedBean(MLoginManagedBean mLoginManagedBean) {
		this.mLoginManagedBean = mLoginManagedBean;
	}

	public String getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(String totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}
	
}
