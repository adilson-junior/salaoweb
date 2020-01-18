package br.com.salao.modelo.vo;

import java.io.Serializable;
import java.util.Date;

public class ChequeVO implements Serializable{	
	
	private static final long serialVersionUID = 1L;	
	private Integer id;
	private Date data;
	private Date dataPagamento;
	private Float valor;
	private String banco;	
	private String cliente;
	
	public ChequeVO(){			
	}	

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
}
