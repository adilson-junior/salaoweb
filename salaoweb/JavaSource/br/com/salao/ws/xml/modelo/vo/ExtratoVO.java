package br.com.salao.ws.xml.modelo.vo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ExtratoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date data;
	private Integer idCliente;
	private String cliente;
	private Float valorTotalComanda;
	private Float descontoTotalComanda;
	private Float total;
	private Float pagamentoCredito;
	private Float pagamentoDebito;
	private Float pagamentoDinheiro;
	private Float pagamentoCheque;
	
	public ExtratoVO(){		
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Float getValorTotalComanda() {
		return valorTotalComanda;
	}

	public void setValorTotalComanda(Float valorTotalComanda) {
		this.valorTotalComanda = valorTotalComanda;
	}

	public Float getDescontoTotalComanda() {
		return descontoTotalComanda;
	}

	public void setDescontoTotalComanda(Float descontoTotalComanda) {
		this.descontoTotalComanda = descontoTotalComanda;
	}

	public Float getPagamentoCredito() {
		return pagamentoCredito;
	}

	public void setPagamentoCredito(Float pagamentoCredito) {
		this.pagamentoCredito = pagamentoCredito;
	}

	public Float getPagamentoDebito() {
		return pagamentoDebito;
	}

	public void setPagamentoDebito(Float pagamentoDebito) {
		this.pagamentoDebito = pagamentoDebito;
	}

	public Float getPagamentoDinheiro() {
		return pagamentoDinheiro;
	}

	public void setPagamentoDinheiro(Float pagamentoDinheiro) {
		this.pagamentoDinheiro = pagamentoDinheiro;
	}

	public Float getPagamentoCheque() {
		return pagamentoCheque;
	}

	public void setPagamentoCheque(Float pagamentoCheque) {
		this.pagamentoCheque = pagamentoCheque;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}	
}
