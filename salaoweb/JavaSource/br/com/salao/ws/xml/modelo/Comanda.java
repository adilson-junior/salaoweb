package br.com.salao.ws.xml.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


public class Comanda implements Serializable, Comparable<Comanda>{	
	
	private static final long serialVersionUID = 1L;
	private Integer id;	
	private Date data;	
	private Date dataPagamento;	
	private Float dinheiro;		
	private Float entrada;		
	private Float valorDesconto;
	private SortedSet<Cartao> cartoes;	
	private Cheque cheque;		
	private SortedSet<Item> itens;	
	private Boolean pendente;	
	private Caixa caixa;	
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
