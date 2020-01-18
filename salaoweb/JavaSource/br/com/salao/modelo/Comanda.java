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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name="comanda_seq", sequenceName="comanda_seq", allocationSize=1)
public class Comanda implements Serializable, Comparable<Comanda>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comanda_seq")
	private Integer id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Column(nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;
	@Column(nullable=true,columnDefinition="numeric")
	private Float dinheiro;	
	@Column(nullable=true,columnDefinition="numeric")
	private Float entrada;	
	@Column(nullable=true,columnDefinition="numeric")
	private Float valorDesconto;
	@OneToMany(mappedBy = "comanda", fetch = FetchType.LAZY, targetEntity = Cartao.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Cartao> cartoes;
	@OneToOne(mappedBy = "comanda", fetch = FetchType.LAZY)
	private Cheque cheque;	
	@OneToMany(mappedBy="comanda", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Item> itens;
	@Column(nullable = false, columnDefinition = "boolean")
	private Boolean pendente;
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Caixa.class)
	@JoinColumn(name="caixa_id",referencedColumnName="id",nullable=false,insertable=true,updatable=true)
	private Caixa caixa;
	@Transient
	private String nomeTemp;
	
	public Comanda(){		
		cartoes = new TreeSet<Cartao>();	
		setItens(new TreeSet<Item>());
		pendente = false;		
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

	public SortedSet<Cartao> getCartoes() {
		return cartoes;
	}

	public void setCartoes(SortedSet<Cartao> cartoes) {
		this.cartoes = cartoes;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}	
	
	public Float getDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(Float dinheiro) {
		this.dinheiro = dinheiro;
	}
	
	public Float getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Float valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	
	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
	}
	
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Boolean getPendente() {
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}
	
	public Float getEntrada() {
		return entrada;
	}

	public void setEntrada(Float entrada) {
		this.entrada = entrada;
	}
	
	public String getNomeTemp() {
		for(Item it : itens){
			nomeTemp = it.getCliente().getNome();
			break;
		}
		return nomeTemp;
	}

	public void setNomeTemp(String nomeTemp) {
		this.nomeTemp = nomeTemp;
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
		Comanda other = (Comanda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Comanda o) {		
        return this.id.compareTo(o.getId());
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

}
