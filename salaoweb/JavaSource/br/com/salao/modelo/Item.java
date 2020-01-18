package br.com.salao.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@SequenceGenerator(name = "item_seq", sequenceName = "item_seq", allocationSize = 1)
public class Item implements Serializable, Comparable<Item> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
	private Integer id;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Profissional.class)
	@JoinColumn(name = "profissional_id", referencedColumnName = "pessoa_id", nullable = false, insertable = true, updatable = true)
	private Profissional profissional;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Servico.class)
	@JoinColumn(name = "servico_id", referencedColumnName = "id", nullable = true, insertable = true, updatable = true)
	private Servico servico;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Produto.class)
	@JoinColumn(name = "produto_id", referencedColumnName = "id", nullable = true, insertable = true, updatable = true)
	private Produto produto;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Cliente.class)
	@JoinColumn(name = "cliente_id", referencedColumnName = "pessoa_id", nullable = false, insertable = true, updatable = true)
	private Cliente cliente;
	@Column(nullable = false, columnDefinition = "numeric")
	private Float valor;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Comanda.class)
	@JoinColumn(name = "comanda_id", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
	private Comanda comanda;
	@Column(nullable = true, columnDefinition = "numeric")
	private Float desconto;
	@Column(nullable = false, columnDefinition = "numeric")
	private Float comissao;
	private Integer quantidade;
	@Column(nullable = false, columnDefinition = "varchar(1)")
	// P = produto, S = Serviço
	private String tipoItem;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Item.class)
	@JoinColumn(name = "item_produto_id", referencedColumnName = "id", nullable = true, insertable = true, updatable = true)
	private Item itemProduto;
	@OneToMany(mappedBy="itemProduto", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Item> itens;	
	@Transient
	public static final String TIPO_PRODUTO = "P";
	@Transient
	public static final String TIPO_SERVICO = "S";
	@Transient
	private String uuid;

	public Item() {
		tipoItem = "S";
		setItens(new TreeSet<Item>());
		quantidade = 1;
		uuid = UUID.randomUUID().toString();
	}
	
	public Item(String tipoItem) {
		this.tipoItem = tipoItem;
		setItens(new TreeSet<Item>());
		quantidade = 1;
		uuid = UUID.randomUUID().toString();
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

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Comanda getComanda() {
		return comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}

	public Float getDesconto() {
		return desconto == null ? 0 : desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Float getComissao() {
		return comissao;
	}

	public void setComissao(Float comissao) {
		this.comissao = comissao;
	}
	
	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	public Item getItemProduto() {
		return itemProduto;
	}

	public void setItemProduto(Item itemProduto) {
		this.itemProduto = itemProduto;
	}

	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getNomeItem(){
		if(produto != null){
			return produto == null ? "" : produto.getNome();
		}else{
			return servico  == null ? "" :  servico.getNome();
		}
	}
	
	@Transient
	public String getUUid() {
		return uuid;
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
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Item o) {		
		if(id == null){
			return 0;
		}
		return this.id.compareTo(o.getId());
	}
	
}
