package br.com.salao.ws.xml.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


public class Caixa implements Serializable, Comparable<Caixa>{	
	
	private static final long serialVersionUID = 1L;
	private Integer id;	
	private Date dataAbertura;
	private Date dataFechamento;
	private Float valorAbertura;	
	private SortedSet<Sangria> sangrias;	
	private SortedSet<Reforco> reforcos;	
	private SortedSet<Comanda> comandas;	
	private String usuarioAbertura;	
	private String usuarioFechamento;	
	private Integer status;	// 0 = fechado, 1 = aberto
	
	public Caixa(){		
		sangrias = new TreeSet<Sangria>();
		reforcos = new TreeSet<Reforco>();
		status = 1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Float getValorAbertura() {
		return valorAbertura;
	}

	public void setValorAbertura(Float valorAbertura) {
		this.valorAbertura = valorAbertura;
	}

	public SortedSet<Sangria> getSangrias() {
		return sangrias;
	}

	public void setSangrias(SortedSet<Sangria> sangrias) {
		this.sangrias = sangrias;
	}

	public SortedSet<Reforco> getReforcos() {
		return reforcos;
	}

	public void setReforcos(SortedSet<Reforco> reforcos) {
		this.reforcos = reforcos;
	}

	public String getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(String usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public String getUsuarioFechamento() {
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(String usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
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
		Caixa other = (Caixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Caixa o) {		
        return this.id.compareTo(o.getId());
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public SortedSet<Comanda> getComandas() {
		return comandas;
	}

	public void setComandas(SortedSet<Comanda> comandas) {
		this.comandas = comandas;
	}

}
