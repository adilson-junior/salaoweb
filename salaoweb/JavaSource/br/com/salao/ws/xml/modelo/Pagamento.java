package br.com.salao.ws.xml.modelo;

import java.io.Serializable;
import java.util.Date;


public class Pagamento implements Serializable, Comparable<Pagamento>{	
	
	private static final long serialVersionUID = 1L;	
	private Integer id;	
	private Date data;	
	private Float valor;	
	private Profissional profissional;	
	private Float vale;	
	private Float outros;
	private Float ajuste;	
	private Date de;	
	private Date ate;	
	private Float total;
	
	public Pagamento(){		
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
	
	public Date getDe() {
		return de;
	}

	public void setDe(Date de) {
		this.de = de;
	}

	public Date getAte() {
		return ate;
	}

	public void setAte(Date ate) {
		this.ate = ate;
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
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Pagamento o) {		
        return this.id.compareTo(o.getId());
	}

	public Float getAjuste() {
		return ajuste;
	}

	public void setAjuste(Float ajuste) {
		this.ajuste = ajuste;
	}

	public Float getTotal() {
		total = valor - Math.abs(vale) - Math.abs(outros);
		if(ajuste > 0){
			total += ajuste;
		}else{
			total -= ajuste;
		}
		return total;
	}

	public void setTotal(Float total) {		
		this.total = total;
	}
}
