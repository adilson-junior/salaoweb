package br.com.salao.ws.xml.modelo;

import java.io.Serializable;


public class Cartao implements Serializable, Comparable<Cartao> {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipoCartao;
	private Float valor;
	private Bandeira bandeira;
	private Comanda comanda;	
	public static final String TIPO_CREDITO = "C";
	public static final String TIPO_DEBITO = "D";

	public Cartao() {		
	}
	
	public Cartao(String tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoCartao() {
		return tipoCartao;
	}

	public void setTipoCartao(String tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Bandeira getBandeira() {
		return bandeira;
	}

	public void setBandeira(Bandeira bandeira) {
		this.bandeira = bandeira;
	}
	
	public Comanda getComanda() {
		return comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Cartao other = (Cartao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Cartao o) {
		return this.id.compareTo(o.getId());
	}

}
