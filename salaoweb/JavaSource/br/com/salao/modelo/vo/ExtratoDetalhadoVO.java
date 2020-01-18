package br.com.salao.modelo.vo;

import java.io.Serializable;
import java.util.Date;

public class ExtratoDetalhadoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date data;
	private String cliente;
	private String item;
	private String tipoItem;
	private Float valor;
	private Float desconto;
	private Float total;
	private Float comissao;
	private Float percentComissao;
	private int qtd;
	
	public ExtratoDetalhadoVO(){		
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Float getDesconto() {
		return desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Float getComissao() {
		return comissao;
	}

	public void setComissao(Float comissao) {
		this.comissao = comissao;
	}

	public Float getPercentComissao() {
		return percentComissao;
	}

	public void setPercentComissao(Float percentComissao) {
		this.percentComissao = percentComissao;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
	
	
}
