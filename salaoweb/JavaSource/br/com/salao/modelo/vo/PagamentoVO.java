package br.com.salao.modelo.vo;

import java.io.Serializable;

public class PagamentoVO implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	private Integer idProfissional;
	private String nomeProfissional;
	private Float valor;	
	private Float vale;	
	private Float outros;
	private Float ajuste;
	private Float total;
	
	public PagamentoVO(){		
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Float getVale() {
		return vale;
	}

	public void setVale(Float vale) {
		this.vale = vale;
	}

	public Float getOutros() {
		return outros;
	}

	public void setOutros(Float outros) {
		this.outros = outros;
	}

	public Float getAjuste() {
		return ajuste;
	}

	public void setAjuste(Float ajuste) {
		this.ajuste = ajuste;
	}
	
	public Integer getIdProfissional() {
		return idProfissional;
	}

	public void setIdProfissional(Integer idProfissional) {
		this.idProfissional = idProfissional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idProfissional == null) ? 0 : idProfissional.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoVO other = (PagamentoVO) obj;
		if (idProfissional == null) {
			if (other.idProfissional != null)
				return false;
		} else if (!idProfissional.equals(other.idProfissional))
			return false;
		return true;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public String getNomeProfissional() {
		return nomeProfissional;
	}

	public void setNomeProfissional(String nomeProfissional) {
		this.nomeProfissional = nomeProfissional;
	}
	
}
