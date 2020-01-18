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
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@SequenceGenerator(name="reforco_seq", sequenceName="reforco_seq", allocationSize=1)
public class Reforco implements Serializable, Comparable<Reforco>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reforco_seq")
	private Integer id;
	@Column(nullable=false,columnDefinition="numeric")
	private Float valor;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Caixa.class)
	@JoinColumn(name="caixa_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Caixa caixa;
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String usuarioAutoriza;
	
	public Reforco(){		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		Reforco other = (Reforco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	
	@Override
	public int compareTo(Reforco o) {		
        return this.id.compareTo(o.getId());
	}

	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}
}
