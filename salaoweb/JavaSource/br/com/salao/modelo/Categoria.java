package br.com.salao.modelo;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

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
@SequenceGenerator(name = "categoria_seq", sequenceName = "categoria_seq", allocationSize = 1)
public class Categoria implements Serializable, Comparable<Categoria> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq")
	private Integer id;
	@Column(nullable = false, columnDefinition = "varchar(100)")
	private String nome;
	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, targetEntity = Produto.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Produto> produtos;
	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, targetEntity = Servico.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Servico> servicos;
	@Column(nullable = false, columnDefinition = "boolean")
	private Boolean ativo;

	public Categoria() {
		produtos = new TreeSet<Produto>();
		servicos = new TreeSet<Servico>();
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

	public SortedSet<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(SortedSet<Servico> servicos) {
		this.servicos = servicos;
	}

	public SortedSet<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(SortedSet<Produto> produtos) {
		this.produtos = produtos;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int compareTo(Categoria o) {
		return this.id.compareTo(o.getId());
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
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return nome;
	}	
}
