package br.com.salao.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name="agenda_seq", sequenceName="agenda_seq", allocationSize=1)
public class Agenda implements Serializable, Comparable<Agenda>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="agenda_seq")
	private Integer id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date data;
	@Column(nullable=false)
	@Temporal(TemporalType.TIME)
	private Date horaDe;
	@Column(nullable=false)
	@Temporal(TemporalType.TIME)
	private Date horaAte;	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="agenda_servico", 
			   joinColumns=@JoinColumn(name="agenda_id"),
			   inverseJoinColumns=@JoinColumn(name="servico_id"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<Servico> servicos;	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Cliente.class)
	@JoinColumn(name="cliente_id",referencedColumnName="pessoa_id",nullable=false,insertable=true,updatable=true)
	private Cliente cliente;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Profissional.class)
	@JoinColumn(name="profissional_id",referencedColumnName="pessoa_id",nullable=false,insertable=true,updatable=true)
	private Profissional profissional;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean fechada;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean encaixe;

	public Agenda(){
		servicos = new TreeSet<Servico>();
		setFechada(new Boolean(false));
		setEncaixe(false);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHoraDe() {
		return horaDe;
	}

	public void setHoraDe(Date horaDe) {
		this.horaDe = horaDe;
	}

	public Date getHoraAte() {
		return horaAte;
	}

	public void setHoraAte(Date horaAte) {
		this.horaAte = horaAte;
	}

	public SortedSet<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(SortedSet<Servico> servicos) {
		this.servicos = servicos;
	}
	
	public Boolean getFechada() {
		return fechada;
	}

	public void setFechada(Boolean fechada) {
		this.fechada = fechada;
	}
	
	public Boolean getEncaixe() {
		return encaixe;
	}

	public void setEncaixe(Boolean encaixe) {
		this.encaixe = encaixe;
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
		Agenda other = (Agenda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Agenda o) {		
        return this.id.compareTo(o.getId());
	}		
}
