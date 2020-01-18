package br.com.salao.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@SequenceGenerator(name="cheque_seq", sequenceName="cheque_seq", allocationSize=1)
public class Cheque implements Serializable, Comparable<Cheque>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cheque_seq")
	private Integer id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;
	@Column(nullable=false,columnDefinition="numeric")
	private Float valor;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Banco.class)
	@JoinColumn(name="banco_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Banco banco;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean pago;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="comanda_id")
	private Comanda comanda;
	
	public Cheque(){	
		pago = new Boolean(false);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
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
		Cheque other = (Cheque) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Cheque o) {		
        return this.id.compareTo(o.getId());
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
}
