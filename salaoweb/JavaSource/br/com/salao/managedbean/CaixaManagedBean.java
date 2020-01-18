package br.com.salao.managedbean;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cartao;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Usuario;
import br.com.salao.servico.FinanceiroServico;

@ManagedBean(name = "caixaMB")
@ViewScoped
public class CaixaManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(value = "#{statusMB}")
	private StatusManagedBean statusManagedBean;
	private Float totalDinheiro;
	private Float totalCredito;
	private Float totalDebito;
	private Float totalCheque;
	private Float totalSangria;
	private Float totalReforco;
	private Float totalEntradaPagamento;
	private Float totalSaida;
	private Float totalEntradaOutros;
	private Float fundoTroco;
	private Float totalVenda;
	private Float totalDesconto;
	private Float saldo;	
	private boolean exibirAbrirCaixa;
	private boolean exibirFecharCaixa;
	private boolean exibirFecharCaixaPendente;
	private String statusCaixa;
	private String usuarioAbertura;
	private Float valorAberturaCaixa;
	private Caixa caixaPendente;

	public CaixaManagedBean() {
		totalDinheiro = 0f;
		totalCredito = 0f;
		totalDebito = 0f;
		totalCheque = 0f;
		totalSangria = 0f;		
		totalReforco = 0f;
		setTotalEntradaPagamento(0f);
		fundoTroco = 0f;
		totalVenda = 0f;
		totalDesconto = 0f;
		totalEntradaPagamento = 0f;
		totalSaida = 0f;
		totalEntradaOutros = 0f;
		saldo = 0f;
		exibirAbrirCaixa = true;
		exibirFecharCaixa = false;
		exibirFecharCaixaPendente = false;
	}

	@PostConstruct
	public void iniciar() {
		validarCaixa();
		calcularSaldo();
	}

	public void calcularSaldo(){
		try {
			if (!exibirFecharCaixa){
				return;
			}
			List<Comanda> comandas = financeiroServico.buscarComandas(caixaPendente.getId());				
			fundoTroco = caixaPendente.getValorAbertura();
			for(Comanda com : comandas){
				if(com.getDinheiro() != null){
					totalDinheiro += com.getDinheiro();
				}
				if(com.getCheque() != null){
					totalCheque += com.getCheque().getValor();
				}
				for(Cartao ct : com.getCartoes()){
					if(ct.getTipoCartao().equals(Cartao.TIPO_CREDITO)){
						totalCredito += ct.getValor();
					}
					if(ct.getTipoCartao().equals(Cartao.TIPO_DEBITO)){
						totalDebito += ct.getValor();
					}
				}
				for(Item it : com.getItens()){
					totalVenda += (it.getValor() * it.getQuantidade());
					totalDesconto += it.getDesconto();
				}
			}
			for (Reforco refo : caixaPendente.getReforcos()){
				totalReforco += refo.getValor();
			}
			for (Sangria sang : caixaPendente.getSangrias()){
				totalSangria += sang.getValor();
			}		
			setTotalEntradaPagamento((totalDinheiro + totalCredito + totalDebito + totalCheque));
			totalEntradaOutros = fundoTroco + totalReforco;
			if(totalSangria > 0){
				totalSaida = totalSangria * -1;
				totalSangria = totalSangria * -1;
			}else{
				totalSaida = totalSangria;
			}
			saldo = (totalEntradaPagamento + totalEntradaOutros) - Math.abs(totalSaida);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Saldo",e.getMessage());
		}
	}

	public void abrir() {
		Caixa caixa = new Caixa();
		Usuario usuario = (Usuario) getRequest().getSession().getAttribute("usuario");
		caixa.setDataAbertura(new Date());
		caixa.setUsuarioAbertura(usuario.getNomeLogin());
		caixa.setValorAbertura(valorAberturaCaixa);
		try {
			financeiroServico.salvarCaixa(caixa);
			redirecionar("pg/caixa/saldo.jsf");
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Abrir Caixa",
					e.getMessage());
		}
		valorAberturaCaixa = null;
	}
	
	public void fecharESair(){
		fechar();
		getRequest().getSession().invalidate();
		redirecionar("login.jsf");
	}
	
	public void fechar(){
		try {
			Usuario usuario = (Usuario) getRequest().getSession().getAttribute("usuario");
			caixaPendente.setUsuarioFechamento(usuario.getNomeLogin());
			caixaPendente.setStatus(0);
			caixaPendente.setDataFechamento(new Date());
			financeiroServico.atualizarCaixa(caixaPendente);
			validarCaixa();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Fechar o Caixa", e.getMessage());
		}
	}

	private void validarCaixa() {
		statusManagedBean.validarCaixa();
		statusCaixa = statusManagedBean.getStatusCaixa();
		usuarioAbertura = statusManagedBean.getUsuarioAbertura();
		exibirAbrirCaixa = statusManagedBean.isExibirAbrirCaixa();
		exibirFecharCaixa = statusManagedBean.isExibirFecharCaixa();
		exibirFecharCaixaPendente = statusManagedBean.isExibirFecharCaixaPendente();
		caixaPendente = statusManagedBean.getCaixaPendente();
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public Float getTotalDinheiro() {
		return totalDinheiro;
	}

	public void setTotalDinheiro(Float totalDinheiro) {
		this.totalDinheiro = totalDinheiro;
	}

	public Float getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(Float totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Float getTotalDebito() {
		return totalDebito;
	}

	public void setTotalDebito(Float totalDebito) {
		this.totalDebito = totalDebito;
	}

	public Float getTotalCheque() {
		return totalCheque;
	}

	public void setTotalCheque(Float totalCheque) {
		this.totalCheque = totalCheque;
	}

	public Float getTotalSangria() {
		return totalSangria;
	}

	public void setTotalSangria(Float totalSangria) {
		this.totalSangria = totalSangria;
	}

	public Float getTotalReforco() {
		return totalReforco;
	}

	public void setTotalReforco(Float totalReforco) {
		this.totalReforco = totalReforco;
	}	

	public StatusManagedBean getStatusManagedBean() {
		return statusManagedBean;
	}

	public void setStatusManagedBean(StatusManagedBean statusManagedBean) {
		this.statusManagedBean = statusManagedBean;
	}

	public boolean isExibirAbrirCaixa() {
		return exibirAbrirCaixa;
	}

	public void setExibirAbrirCaixa(boolean exibirAbrirCaixa) {
		this.exibirAbrirCaixa = exibirAbrirCaixa;
	}

	public boolean isExibirFecharCaixa() {
		return exibirFecharCaixa;
	}

	public void setExibirFecharCaixa(boolean exibirFecharCaixa) {
		this.exibirFecharCaixa = exibirFecharCaixa;
	}

	public boolean isExibirFecharCaixaPendente() {
		return exibirFecharCaixaPendente;
	}

	public void setExibirFecharCaixaPendente(boolean exibirFecharCaixaPendente) {
		this.exibirFecharCaixaPendente = exibirFecharCaixaPendente;
	}

	public String getStatusCaixa() {
		return statusCaixa;
	}

	public void setStatusCaixa(String statusCaixa) {
		this.statusCaixa = statusCaixa;
	}

	public String getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(String usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public Float getValorAberturaCaixa() {
		return valorAberturaCaixa;
	}

	public void setValorAberturaCaixa(Float valorAberturaCaixa) {
		this.valorAberturaCaixa = valorAberturaCaixa;
	}

	public Float getFundoTroco() {
		return fundoTroco;
	}

	public void setFundoTroco(Float fundoTroco) {
		this.fundoTroco = fundoTroco;
	}

	public Caixa getCaixaPendete() {
		return caixaPendente;
	}

	public void setCaixaPendete(Caixa caixaPendete) {
		this.caixaPendente = caixaPendete;
	}

	public Float getTotalVenda() {
		return totalVenda;
	}

	public void setTotalVenda(Float totalVenda) {
		this.totalVenda = totalVenda;
	}

	public Float getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(Float totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public Float getTotalEntradaPagamento() {
		return totalEntradaPagamento;
	}

	public void setTotalEntradaPagamento(Float totalEntradaPagamento) {
		this.totalEntradaPagamento = totalEntradaPagamento;
	}

	public Float getTotalSaida() {
		return totalSaida;
	}

	public void setTotalSaida(Float totalSaida) {
		this.totalSaida = totalSaida;
	}

	public Float getTotalEntradaOutros() {
		return totalEntradaOutros;
	}

	public void setTotalEntradaOutros(Float totalEntradaOutros) {
		this.totalEntradaOutros = totalEntradaOutros;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}
}
