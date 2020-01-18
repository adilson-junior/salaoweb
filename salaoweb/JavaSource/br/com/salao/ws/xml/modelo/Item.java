package br.com.salao.ws.xml.modelo;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;


public class Item implements Serializable, Comparable<Item> {

	private static final long serialVersionUID = 1L;
	private Integer id;	
	private Date data;
	private Profissional profissional;	
	private Servico servico;	
	private Produto produto;	
	private Cliente cliente;	
	private Float valor;	
	private Comanda comanda;	
	private Float desconto;	
	private Float comissao;
	private Integer quantidade;
	// P = produto, S = Serviço
	private String tipoItem;
	private Item itemProduto;
	private SortedSet<Item> itens;		
	public static final String TIPO_PRODUTO = "P";	
	public static final String TIPO_SERVICO = "S";	
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
