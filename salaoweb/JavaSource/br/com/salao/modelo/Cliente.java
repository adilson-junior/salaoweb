package br.com.salao.modelo;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@PrimaryKeyJoinColumn(name="pessoa_id")
public class Cliente extends Pessoa{
	
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY, targetEntity=Agenda.class)
	@Sort(type = SortType.NATURAL)	
	private SortedSet<Agenda> agendas;
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)	
	private SortedSet<Item> itens;
	
	public Cliente(){			
		setAgendas(new TreeSet<Agenda>());
		setItens(new TreeSet<Item>());		
	}

	public SortedSet<Agenda> getAgendas() {
		return agendas;
	}

	public void setAgendas(SortedSet<Agenda> agendas) {
		this.agendas = agendas;
	}

	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
	}
	
	public void incluirItemComanda(Item item){
		itens.add(item);
	}

}
