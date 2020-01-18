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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name="servico_seq", sequenceName="servico_seq", allocationSize=1)
public class Servico implements Serializable, Comparable<Servico>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="servico_seq")
	private Integer id;
	@Column(nullable=false,columnDefinition="numeric")
	private Float preco;
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String nome;
	@Column(nullable=true,columnDefinition="varchar(300)")
	private String detalhes;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean ativo;
	@Column(nullable=false,columnDefinition="integer")
	private Integer duracao;
	@Column(nullable=true,columnDefinition="numeric")
	private Float comissao;
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="agenda_servico", 
			   joinColumns=@JoinColumn(name="servico_id"),
			   inverseJoinColumns=@JoinColumn(name="agenda_id"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<Agenda> agendas;	
	@OneToMany(mappedBy="servico", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Item> itens;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Categoria.class)
	@JoinColumn(name="categoria_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Categoria categoria;
	
	public Servico(){	
		ativo = new Boolean(true);
		setAgendas(new TreeSet<Agenda>());		
		setItens(new TreeSet<Item>());
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

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public SortedSet<Agenda> getAgendas() {
		return agendas;
	}

	public void setAgendas(SortedSet<Agenda> agendas) {
		this.agendas = agendas;
	}
	
	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}
	
	public Float getComissao() {
		return comissao;
	}

	public void setComissao(Float comissao) {
		this.comissao = comissao;
	}

	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
		Servico other = (Servico) obj;
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
	
	@Override
	public int compareTo(Servico o) {		
        return this.id.compareTo(o.getId());
	}	
}
