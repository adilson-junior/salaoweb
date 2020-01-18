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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name="produto_seq", sequenceName="produto_seq", allocationSize=1)
public class Produto implements Serializable, Comparable<Produto>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="produto_seq")
	private Integer id;
	@Column(nullable=false,columnDefinition="numeric")
	private Float preco;
	@Column(nullable=false,columnDefinition="numeric")
	private Float custo;
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String nome;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean ativo;	
	@Column(nullable=true,columnDefinition="numeric")
	private Float comissao;
	@Column(nullable=true,columnDefinition="numeric")
	private Float participacao;
	@Column(nullable=true,columnDefinition="integer")
	private Integer quantidade;		
	@OneToMany(mappedBy="produto", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Item> itens;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Categoria.class)
	@JoinColumn(name="categoria_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Categoria categoria;
	
	public Produto(){	
		setItens(new TreeSet<Item>());
		ativo = new Boolean(true);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getPreco() {
		return preco;
	}

	public void setPreco(Float preco) {
		this.preco = preco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Float getComissao() {
		return comissao;
	}

	public void setComissao(Float comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public Float getCusto() {
		return custo;
	}

	public void setCusto(Float custo) {
		this.custo = custo;
	}
	
	public Float getParticipacao() {
		return participacao;
	}

	public void setParticipacao(Float participacao) {
		this.participacao = participacao;
	}	
	
	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Produto o) {		
        return this.id.compareTo(o.getId());
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public String toString() {
		return nome;
	}	
}
