package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
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
import br.com.salao.modelo.vo.ExtratoVO;
import br.com.salao.servico.FinanceiroServico;

@ManagedBean(name = "extratoMB")
@ViewScoped
public class ExtratoManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(value = "#{statusMB}")
	private StatusManagedBean statusManagedBean;
	private List<ExtratoVO> extratos;
	private List<Reforco> reforcos;
	private List<Sangria> sangrias;
	private Float totalValor;
	private Float totalDesconto;
	private Float totalComanda;
	private Float totalDinheiro;
	private Float totalCredito;
	private Float totalDebito;
	private Float totalCheque;
	private String totalSangria;
	private String totalReforco;
	private String largura;
	
	public ExtratoManagedBean(){	
		extratos = new ArrayList<ExtratoVO>();
		reforcos = new ArrayList<Reforco>();
		sangrias = new ArrayList<Sangria>();	
		totalValor = 0f;
		totalDesconto = 0f;
		totalComanda = 0f;
		totalDinheiro = 0f;
		totalCredito = 0f;
		totalDebito = 0f;
		totalCheque = 0f;
		totalSangria = "";
		totalReforco = "";
	}
	
	@PostConstruct
	public void iniciar(){		
		calcularExtrato();
	}
	
	private void calcularExtrato(){
		try {
			Caixa caixa = statusManagedBean.getCaixaPendente();
			List<Comanda> comandas = financeiroServico.buscarComandas(caixa.getId());			
			reforcos.addAll(caixa.getReforcos());
			sangrias.addAll(caixa.getSangrias());	
			Float totalSagria = 0f;
			Float totalReforco = 0f;
			for(Sangria sa : sangrias){
				totalSangria += sa.getValor();
			}
			for(Reforco ref : reforcos){
				totalReforco += ref.getValor();
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();			
			this.totalSangria = nFormat.format(totalSagria);
			this.totalReforco = nFormat.format(totalReforco);
			if(comandas.size() > 9){
				largura = "83";
			}else{
				largura = "";
			}
			for(Comanda com : comandas){
				String nomeCliente = null;
				Float totalDinheiro = 0F;
				Float totalCredito = 0F;
				Float totalDebito = 0F;
				Float totalCheque = 0F;
				Float totalVenda = 0F;
				Float totalDesconto = 0F;
				if(com.getDinheiro() != null){
					totalDinheiro += com.getDinheiro();
					this.totalDinheiro += com.getDinheiro();
				}
				if(com.getCheque() != null){
					totalCheque += com.getCheque().getValor();
					this.totalCheque += com.getCheque().getValor();
				}
				for(Cartao ct : com.getCartoes()){
					if(ct.getTipoCartao().equals(Cartao.TIPO_CREDITO)){
						totalCredito += ct.getValor();
						this.totalCredito += ct.getValor();
					}
					if(ct.getTipoCartao().equals(Cartao.TIPO_DEBITO)){
						totalDebito += ct.getValor();
						this.totalDebito += ct.getValor();
					}
				}
				for(Item it : com.getItens()){
					if(nomeCliente == null){
						nomeCliente = it.getCliente().getNome();
					}
					totalVenda += (it.getValor() * it.getQuantidade());
					totalDesconto += it.getDesconto();
					this.totalValor += (it.getValor() * it.getQuantidade());
					this.totalDesconto += it.getDesconto();					
				}
				ExtratoVO extrato = new ExtratoVO();
				extrato.setData(com.getDataPagamento());
				extrato.setCliente(nomeCliente);
				if(totalDesconto > 0){
					extrato.setDescontoTotalComanda(totalDesconto * -1);
				}else{
					extrato.setDescontoTotalComanda(totalDesconto);
				}
				extrato.setValorTotalComanda(totalVenda);
				extrato.setTotal(totalVenda -  totalDesconto);
				extrato.setPagamentoCheque(totalCheque);
				extrato.setPagamentoCredito(totalCredito);
				extrato.setPagamentoDebito(totalDebito);
				extrato.setPagamentoDinheiro(totalDinheiro);
				extratos.add(extrato);
				this.totalComanda = this.totalValor - this.totalDesconto;
				if(this.totalDesconto > 0){
					this.totalDesconto =  this.totalDesconto * -1;
				}
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato",e.getMessage());
		}
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public List<ExtratoVO> getExtratos() {
		return extratos;
	}

	public void setExtratos(List<ExtratoVO> extratos) {
		this.extratos = extratos;
	}

	public List<Reforco> getReforcos() {
		return reforcos;
	}

	public void setReforcos(List<Reforco> reforcos) {
		this.reforcos = reforcos;
	}

	public List<Sangria> getSangrias() {
		return sangrias;
	}

	public void setSangrias(List<Sangria> sangrias) {
		this.sangrias = sangrias;
	}
	
	public StatusManagedBean getStatusManagedBean() {
		return statusManagedBean;
	}

	public void setStatusManagedBean(StatusManagedBean statusManagedBean) {
		this.statusManagedBean = statusManagedBean;
	}

	public Float getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(Float totalValor) {
		this.totalValor = totalValor;
	}

	public Float getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(Float totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public Float getTotalComanda() {
		return totalComanda;
	}

	public void setTotalComanda(Float totalComanda) {
		this.totalComanda = totalComanda;
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

	public String getTotalSangria() {
		return totalSangria;
	}

	public void setTotalSangria(String totalSangria) {
		this.totalSangria = totalSangria;
	}

	public String getTotalReforco() {
		return totalReforco;
	}

	public void setTotalReforco(String totalReforco) {
		this.totalReforco = totalReforco;
	}

	public String getLargura() {
		return largura;
	}

	public void setLargura(String largura) {
		this.largura = largura;
	}	
}
