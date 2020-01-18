package br.com.salao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@SequenceGenerator(name = "cartao_seq", sequenceName = "cartao_seq", allocationSize = 1)
public class Cartao implements Serializable, Comparable<Cartao> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartao_seq")
	private Integer id;
	@Column(nullable = false, columnDefinition = "varchar(1)")
	private String tipoCartao;
	@Column(nullable = false, columnDefinition = "numeric")
	private Float valor;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Bandeira.class)
	@JoinColumn(name = "bandeira_id", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
	private Bandeira bandeira;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Comanda.class)
	@JoinColumn(name="comanda_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Comanda comanda;	
	@Transient
	public static final String TIPO_CREDITO = "C";
	@Transient
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
