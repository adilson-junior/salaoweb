package br.com.salao.modelo;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name = "bandeira_seq", sequenceName = "bandeira_seq", allocationSize = 1)
public class Bandeira implements Serializable, Comparable<Bandeira> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bandeira_seq")
	private Integer id;
	@Column(nullable = false, columnDefinition = "varchar(20)")
	private String nome;
	@OneToMany(mappedBy = "bandeira", fetch = FetchType.LAZY, targetEntity = Cartao.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Cartao> cartoes;
	@Column(nullable = false, columnDefinition = "boolean")
	private Boolean ativo;

	public Bandeira() {
		setCartoes(new TreeSet<Cartao>());
		ativo = new Boolean(true);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public SortedSet<Cartao> getCartoes() {
		return cartoes;
	}

	public void setCartoes(SortedSet<Cartao> cartoes) {
		this.cartoes = cartoes;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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
		Bandeira other = (Bandeira) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Bandeira o) {
		return this.id.compareTo(o.getId());
	}
	
	@Override
	public String toString() {
		return nome;
	}	
}
