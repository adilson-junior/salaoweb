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
@SequenceGenerator(name="outros_seq", sequenceName="outros_seq", allocationSize=1)
public class Outros implements Serializable, Comparable<Outros>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="outros_seq")
	private Integer id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Column(nullable=false,columnDefinition="numeric")
	private Float valor;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Profissional.class)
	@JoinColumn(name="profissional_id",referencedColumnName="pessoa_id",nullable=false,insertable=true,updatable=true)
	private Profissional profissional;
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String usuarioAutoriza;
	@Column(nullable=false,columnDefinition="varchar(200)")
	private String descricao;
	
	public Outros(){		
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

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
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
		Outros other = (Outros) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Outros o) {		
        return this.id.compareTo(o.getId());
	}

	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
