package br.com.salao.ws.xml.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class Agenda implements Serializable, Comparable<Agenda>{	
	
	private static final long serialVersionUID = 1L;	
	private Integer id;	
	private Date dataCriacao;
	private Date data;	
	private Date horaDe;
	private Date horaAte;		
	private SortedSet<Servico> servicos;		
	private Cliente cliente;	
	private Profissional profissional;
	private Boolean fechada;
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
