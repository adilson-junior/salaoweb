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
@SequenceGenerator(name="sangria_seq", sequenceName="sangria_seq", allocationSize=1)
public class Sangria implements Serializable, Comparable<Sangria>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sangria_seq")
	private Integer id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Column(nullable=false,columnDefinition="numeric")
	private Float valor;
	@Column(nullable=false,columnDefinition="varchar(200)")
	private String motivo;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Caixa.class)
	@JoinColumn(name="caixa_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Caixa caixa;
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String usuarioAutoriza;
	
	public Sangria(){		
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
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
		Sangria other = (Sangria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
	@Override
	public int compareTo(Sangria o) {		
        return this.id.compareTo(o.getId());
	}

	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}
}
