package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Banco;
import br.com.salao.modelo.Bandeira;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cartao;
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Cliente;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Produto;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Servico;
import br.com.salao.servico.AgendaServico;
import br.com.salao.servico.BancoServico;
import br.com.salao.servico.BandeiraServico;
import br.com.salao.servico.ClienteServico;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProdutoServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.ServicoServico;

@ManagedBean(name="comandaMB")
@ViewScoped
public class ComandaManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private List<Cliente> clientes;
	private List<Cliente> clientesAgendas;
	private List<Comanda> comandasFechadas;
	private List<Comanda> comandasTemp;
	private List<Profissional> profissionais;
	private List<Servico> servicos;
	private List<Produto> produtos;
	private List<Item> itens;
	private List<Banco> bancos;
	private List<Bandeira> bandeiras;
	private Banco bancoSelecionado;
	private Bandeira bandeiraSelecionada;
	private Cartao cartaoCredito;
	private Cartao cartaoDebito;
	private Cheque cheque;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(name="clienteServico",value="#{clienteServico}")
	private ClienteServico clienteServico;
	@ManagedProperty(name="servicoServico",value="#{servicoServico}")
	private ServicoServico servicoServico;	
	@ManagedProperty(name="produtoServico",value="#{produtoServico}")
	private ProdutoServico produtoServico;
	@ManagedProperty(name="bandeiraServico",value="#{bandeiraServico}")
	private BandeiraServico bandeiraServico;
	@ManagedProperty(name="bancoServico",value="#{bancoServico}")
	private BancoServico bancoServico;
	@ManagedProperty(name="agendaServico",value="#{agendaServico}")
	private AgendaServico agendaServico;
	@ManagedProperty(name="financeiroServico",value="#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(value="#{statusMB}")
	private StatusManagedBean statusManagedBean;
	private boolean bloquearEventos;
	private boolean bloquearInputCredito;
	private boolean bloquearInputDebito;
	private boolean bloquearInputCheque;
	private boolean bloquearListasComandas;
	private Cliente clienteSelecionado;
	private Cliente clienteNovaComanda;
	private String titulo;
	private Float subTotal;
	private Float totalValor;
	private Float totalDesconto;
	private Float entrada;
	private int totalIntens;
	private Item itemSelecionado;
	private Float desconto;
	private Comanda comanda;	
	private DualListModel<Servico> dualServicos;
	private DualListModel<Produto> dualProdutos;
	
	public ComandaManagedBean(){
		dualServicos = new DualListModel<Servico>(new ArrayList<Servico>(), new ArrayList<Servico>());
		dualProdutos = new DualListModel<Produto>(new ArrayList<Produto>(), new ArrayList<Produto>());
		profissionais = new ArrayList<Profissional>();
		clientes = new ArrayList<Cliente>();
		servicos = new ArrayList<Servico>();
		produtos = new ArrayList<Produto>();
		itens = new ArrayList<Item>();
		clientesAgendas = new ArrayList<Cliente>();
		comandasFechadas = new ArrayList<Comanda>();
		bancoSelecionado = new Banco();
		bandeiraSelecionada = new Bandeira();
		cartaoCredito = new Cartao(Cartao.TIPO_CREDITO);		
		cartaoDebito = new Cartao(Cartao.TIPO_DEBITO);
		cheque = new Cheque();
		bloquearEventos = true;
		setTitulo("");
		subTotal = 0f;
		totalValor = 0f;
		totalDesconto = 0f;
		itemSelecionado = new Item();
		desconto = 0f;
		bloquearInputCredito = true;
		bloquearInputDebito = true;
		bloquearInputCheque = true;
		bloquearListasComandas = false;
		comanda = new Comanda();
	}
	
	@PostConstruct
	public void iniciar(){
		listarClientes();
		listarProfissionais();
		listarBancos();
		listarBandeiras();
		listarServicos();
		listarProdutos();
		listarComandasFechadas();
	}
	
	public void comandaAbertaSelecionado(SelectEvent event){
		bloquearEventos = false;
		clienteSelecionado = (Cliente) event.getObject();
		titulo = " - "+clienteSelecionado.getNome() + " "+clienteSelecionado.getTelefone();
		carregarItensComanda(clienteSelecionado.getAgendas());
	}
	
	public void comandaFechadaSelecionado(SelectEvent event){	
		bloquearEventos = true;	
		comanda = (Comanda) event.getObject();		
		SortedSet<Item> itens = comanda.getItens();
		this.itens.clear();	
		this.itens.addAll(itens);
		totalIntens = 0;
		totalValor = 0f;
		subTotal = 0f;
		totalDesconto = 0f;	
		titulo = null;
		for(Item it : itens){
			if(titulo == null){
				titulo = " - "+it.getCliente().getNome() + " "+it.getCliente().getTelefone();
			}
			subTotal += ((it.getQuantidade() * it.getValor()) - it.getDesconto());		
			totalValor += (it.getQuantidade() * it.getValor());	
			totalDesconto += it.getDesconto();
			totalIntens += it.getQuantidade();
		}	
		
	}
	
	public void novaComanda(){				
		clienteSelecionado = clienteNovaComanda;		
		clienteSelecionado.setAgendas(new TreeSet<Agenda>());
		titulo = " (Nova) - "+clienteSelecionado.getNome() + " "+clienteSelecionado.getTelefone();
		itens.clear();
		totalIntens = 0;
		totalValor = 0f;
		subTotal = 0f;
		totalDesconto = 0f;
		clienteNovaComanda = new Cliente();
		bloquearListasComandas = true;
		bloquearEventos = false;
	}
	
	public void cancelarComanda(){
		boolean erro = false;
		if(clienteSelecionado.getAgendas().size() > 0){
			List<Agenda> agendas = new ArrayList<Agenda>();
			agendas.addAll(clienteSelecionado.getAgendas());
			try {
				agendaServico.fechar(agendas);
				clientesAgendas.remove(clienteSelecionado);
			} catch (ServicoException e) {
				erro = true;
				addMensagem(FacesMessage.SEVERITY_ERROR, "Cancelar",e.getMessage());
			}			
		}
		if(!erro){
			titulo = "";
			itens.clear();
			totalIntens = 0;
			totalValor = 0f;
			subTotal = 0f;
			totalDesconto = 0f;
			clienteNovaComanda = new Cliente();
			clienteSelecionado = new Cliente();
			bloquearListasComandas = false;
			bloquearEventos = true;
		}	
	}
	
	public void incluirProduto(){
		if(itemSelecionado.getProfissional() == null || dualProdutos.getTarget().size() == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Incluir Item", "Selecione um Profissional e uma ou mais produtos.");
			return;
		}else{
			List<Produto> produtos = dualProdutos.getTarget();
			for(Produto p : produtos){
				p = this.produtos.get(this.produtos.indexOf(p));
				Item item = new Item(Item.TIPO_PRODUTO);			
				item.setCliente(clienteSelecionado);
				item.setValor(p.getPreco());
				item.setComissao(p.getComissao());
				item.setProduto(p);
				item.setProfissional(itemSelecionado.getProfissional());
				itens.add(item);
				subTotal += p.getPreco();
				totalValor += p.getPreco();
			}
			totalIntens = itens.size();
		}	
		dualProdutos.getSource().clear();
		dualProdutos.getSource().addAll(produtos);
		dualProdutos.getTarget().clear();
		itemSelecionado = new Item();
	}
	
	public void prepararIncluirProduto(){
		dualProdutos.getSource().clear();
		dualProdutos.getSource().addAll(produtos);
		dualProdutos.getTarget().clear();
		itemSelecionado = new Item();
	}
	
	public void incluirServico(){
		if(itemSelecionado.getProfissional() == null || dualServicos.getTarget().size() == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Incluir Item", "Selecione um Profissional e um ou mais serviços.");
			return;
		}else{
			List<Servico> servicos = dualServicos.getTarget();
			for(Servico s : servicos){
				s = this.servicos.get(this.servicos.indexOf(s));
				Item item = new Item(Item.TIPO_SERVICO);			
				item.setCliente(clienteSelecionado);
				item.setValor(s.getPreco());
				item.setComissao(s.getComissao());
				item.setServico(s);
				item.setProfissional(itemSelecionado.getProfissional());
				itens.add(item);
				subTotal += s.getPreco();
				totalValor += s.getPreco();
			}
			totalIntens = itens.size();
		}	
		dualServicos.getSource().clear();
		dualServicos.getSource().addAll(servicos);
		dualServicos.getTarget().clear();
		itemSelecionado = new Item();
	}
	
	public void prepararIncluirServico(){
		dualServicos.getSource().clear();
		dualServicos.getSource().addAll(servicos);
		dualServicos.getTarget().clear();
		itemSelecionado = new Item();
	}
	
	private void carregarItensComanda(SortedSet<Agenda> agendas){
		itens.clear();
		totalIntens = 0;
		totalValor = 0f;
		subTotal = 0f;
		totalDesconto = 0f;
		for(Agenda ag : agendas){
			for(Servico sv : ag.getServicos()){
				subTotal += sv.getPreco();
				Item item = new Item();
				item.setCliente(clienteSelecionado);
				item.setValor(sv.getPreco());
				item.setComissao(sv.getComissao());
				item.setServico(sv);
				item.setProfissional(ag.getProfissional());
				itens.add(item);				
			}
		}
		totalIntens = itens.size();
		totalValor = subTotal;
	}
	
	public void cancelarPagamento(){
		cartaoCredito = new Cartao(Cartao.TIPO_CREDITO);		
		cartaoDebito = new Cartao(Cartao.TIPO_DEBITO);
		cheque = new Cheque();	
		bloquearInputCredito = true;
		bloquearInputDebito = true;
		bloquearInputCheque = true;
		entrada = null;
	}
	
	public void pagamento(){		
		Comanda comanda = new Comanda();
		Float totalEntradaCartaoCheque = 0f;		
		Float total = 0f;
		Date data = new Date();	
		for(Item item : this.itens){
			item.setData(data);		
			total += (item.getValor() * item.getQuantidade()) - item.getDesconto();
		}		
		List<Cartao> cartoes = new ArrayList<Cartao>();
		if(cartaoCredito != null && cartaoCredito.getValor() != null && cartaoCredito.getValor() > 0){		
			totalEntradaCartaoCheque += cartaoCredito.getValor();
			cartoes.add(cartaoCredito);
		}
		if(cartaoDebito != null && cartaoDebito.getValor() != null && cartaoDebito.getValor() > 0){		
			totalEntradaCartaoCheque += cartaoDebito.getValor();
			cartoes.add(cartaoDebito);
		}
		if(cheque != null && cheque.getValor() != null && cheque.getValor() > 0){	
			totalEntradaCartaoCheque += cheque.getValor();
			cheque.setData(data);
			comanda.setCheque(cheque);
		}	
		if(entrada == null){
			entrada = 0f;
		}
		comanda.setData(data);
		comanda.setDataPagamento(data);
		if(total == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Concluir Comanda", "Adicione itens na comanda");
			cancelarPagamento();
			return;
		}	
		if((totalEntradaCartaoCheque + entrada) < total){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Concluir Comanda", "O valor total de entrada é menor que o total da comanda");
			cancelarPagamento();
			return;
		}	
		Float dinheiro = 0f;			
		if(entrada > total){
			dinheiro = entrada - total;
		}else{
			dinheiro = entrada;
		}
		comanda.setDinheiro(dinheiro);
		comanda.setItens(null);
		List<Agenda> agendas = new ArrayList<Agenda>();		
		agendas.addAll(clienteSelecionado.getAgendas());
		try {
			comanda.setCaixa(statusManagedBean.getCaixaPendente());
			financeiroServico.salvar(comanda, itens, cartoes, agendas);
			if(clienteSelecionado.getAgendas().size() >  0){
				clientesAgendas.remove(clienteSelecionado);
			}
			cartaoCredito = new Cartao(Cartao.TIPO_CREDITO);		
			cartaoDebito = new Cartao(Cartao.TIPO_DEBITO);
			cheque = new Cheque();			
			bloquearInputCredito = true;
			bloquearInputDebito = true;
			bloquearInputCheque = true;			
			titulo = "";
			itens.clear();
			totalIntens = 0;
			totalValor = 0f;
			subTotal = 0f;
			totalDesconto = 0f;		
			entrada = null;
			clienteSelecionado = new Cliente();
			bloquearListasComandas = false;
			bloquearEventos = true;		
			listarComandasFechadas();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Concluir Comanda", e.getMessage());
		}
	}
	
	public void editarItem(){
		if(itemSelecionado.getDesconto() == (itemSelecionado.getValor() * itemSelecionado.getQuantidade())){			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Comanda","O desconto é igual ao valor do item: Desconto "+itemSelecionado.getDesconto()+" = Item "+(itemSelecionado.getValor() * itemSelecionado.getQuantidade()));		
			itemSelecionado.setDesconto(0f);
		}
		if(itemSelecionado.getDesconto() > (itemSelecionado.getValor() * itemSelecionado.getQuantidade())){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Comanda","O desconto é maior que o valor do item: Desconto "+itemSelecionado.getDesconto()+" > Item "+(itemSelecionado.getValor() * itemSelecionado.getQuantidade()));
			itemSelecionado.setDesconto(0f);
		}		
		totalIntens = 0;
		totalValor = 0f;
		subTotal = 0f;
		totalDesconto = 0f;
		for(Item it : itens){
			subTotal += ((it.getQuantidade() * it.getValor()) - it.getDesconto());		
			totalValor += (it.getQuantidade() * it.getValor());	
			totalDesconto += it.getDesconto();
			totalIntens += it.getQuantidade();
		}		
		itemSelecionado = new Item();
	}
	
	public void excluirItem(){			
		int index = 0;
		for(Item it : itens){
			if(it.getUUid().equals(itemSelecionado.getUUid())){				
				itens.remove(index);
				break;
			}
			index++;
		}	
		subTotal = 0f;
		totalValor = 0f;	
		totalIntens = 0;
		totalDesconto = 0f;
		if(itens.size() > 0){			
			for(Item it : itens){				
				subTotal += ((it.getQuantidade() * it.getValor()) - it.getDesconto());		
				totalValor += (it.getQuantidade() * it.getValor());
				totalDesconto += it.getDesconto();
				totalIntens += it.getQuantidade();
			}
		}	
		itemSelecionado = new Item();
	}
	
	public void aplicarDesconto(){
		if(desconto == subTotal){
			addMensagem(FacesMessage.SEVERITY_WARN, "Comanda","O desconto da comanda é igual ao subtotal");
			return;
		}
		if(desconto > subTotal){
			addMensagem(FacesMessage.SEVERITY_WARN, "Comanda","O desconto da comanda é maior que o subtotal");
			return;
		}
		totalValor = 0f;
		subTotal = 0f;
		float descontoItem = desconto / itens.size();
		for(Item it : itens){
			it.setDesconto(descontoItem);
			subTotal += ((it.getQuantidade() * it.getValor()) - it.getDesconto());		
			totalValor += (it.getQuantidade() * it.getValor());	
		}
		totalDesconto = desconto;
		desconto = 0f;
	}
	
	public List<Profissional> pesquisarProfissional(String chave){ 
		List<Profissional> profis = new ArrayList<Profissional>();
		for(Profissional prof : profissionais){
			if(prof.getNome().toLowerCase().startsWith(chave)){
				profis.add(prof);
			}
		}
		return profis;
	}
	
	public void listarProfissionais(){
		try {
			profissionais = profissionalServico.listarProfissionalPagamentoComissao();
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public List<Cliente> pesquisarCliente(String chave){ 
		chave = chave.trim();
		List<Cliente> clis = new ArrayList<Cliente>();
		for(Cliente cli : clientes){
			if(cli.getNome().toLowerCase().startsWith(chave)){
				clis.add(cli);
			}
		}
		return clis;
	}
	
	public void listarClientes(){
		try {
			clientes = clienteServico.listar(true);
			clientesAgendas = clienteServico.buscarClientesEAgendas(new Date());
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void listarProdutos(){
		try {
			produtos = produtoServico.listar(true);
			dualProdutos.getSource().addAll(produtos);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void listarServicos(){
		try {
			servicos = servicoServico.listar(true);
			dualServicos.getSource().addAll(servicos);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	private void listarBancos(){
		try {
			bancos = bancoServico.listar(true);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	private void listarBandeiras(){
		try {
			bandeiras = bandeiraServico.listar(true);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void atualizarInputDebito(SelectEvent event){
		Object obj = event.getObject();
		if(obj == null){
			cartaoDebito = new Cartao(Cartao.TIPO_DEBITO);
			bloquearInputDebito = true;
		}else{
			bloquearInputDebito = false;
		}
	}
	
	public void atualizarInputCredito(SelectEvent event){
		Object obj = event.getObject();
		if(obj == null){
			cartaoCredito = new Cartao(Cartao.TIPO_CREDITO);
			bloquearInputCredito = true;
		}else{
			bloquearInputCredito = false;
		}
	}
	
	public void atualizarInputCheque(SelectEvent event){
		Object obj = event.getObject();
		if(obj == null){
			cheque = new Cheque();
			bloquearInputCheque = true;
		}else{
			bloquearInputCheque = false;
		}
	}
	
	private void listarComandasFechadas(){
		try {
			Caixa caixa = statusManagedBean.getCaixaPendente();
			comandasFechadas.clear();
			comandasTemp = financeiroServico.buscarComandasFechadas(caixa.getId());
			comandasFechadas.addAll(comandasTemp);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());
		}
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public void setClienteServico(ClienteServico clienteServico) {
		this.clienteServico = clienteServico;
	}

	public void setServicoServico(ServicoServico servicoServico) {
		this.servicoServico = servicoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}
	
	public boolean isBloquearEventos() {
		return bloquearEventos;
	}

	public void setBloquearEventos(boolean bloquearEventos) {
		this.bloquearEventos = bloquearEventos;
	}

	public List<Cliente> getClientesAgendas() {
		return clientesAgendas;
	}

	public void setClientesAgendas(List<Cliente> clientesAgendas) {
		this.clientesAgendas = clientesAgendas;
	}

	public List<Comanda> getComandasFechadas() {
		return comandasFechadas;
	}

	public void setComandasFechadas(List<Comanda> comandasFechadas) {
		this.comandasFechadas = comandasFechadas;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Float subTotal) {
		this.subTotal = subTotal;
	}

	public Float getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(Float totalValor) {
		this.totalValor = totalValor;
	}

	public Float getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(Float totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public int getTotalIntens() {
		return totalIntens;
	}

	public void setTotalIntens(int totalIntens) {
		this.totalIntens = totalIntens;
	}

	public Item getItemSelecionado() {
		return itemSelecionado;
	}

	public void setItemSelecionado(Item itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	public Float getDesconto() {
		return desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}
	
	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public void setBandeiraServico(BandeiraServico bandeiraServico) {
		this.bandeiraServico = bandeiraServico;
	}

	public void setBancoServico(BancoServico bancoServico) {
		this.bancoServico = bancoServico;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public List<Bandeira> getBandeiras() {
		return bandeiras;
	}

	public Banco getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(Banco bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public Bandeira getBandeiraSelecionada() {
		return bandeiraSelecionada;
	}

	public void setBandeiraSelecionada(Bandeira bandeiraSelecionada) {
		this.bandeiraSelecionada = bandeiraSelecionada;
	}

	public Cartao getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(Cartao cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

	public Cartao getCartaoDebito() {
		return cartaoDebito;
	}

	public void setCartaoDebito(Cartao cartaoDebito) {
		this.cartaoDebito = cartaoDebito;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	public boolean isBloquearInputCredito() {
		return bloquearInputCredito;
	}

	public boolean isBloquearInputDebito() {
		return bloquearInputDebito;
	}

	public boolean isBloquearInputCheque() {
		return bloquearInputCheque;
	}

	public Comanda getComanda() {
		return comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}

	public DualListModel<Servico> getDualServicos() {
		return dualServicos;
	}

	public void setDualServicos(DualListModel<Servico> dualServicos) {
		this.dualServicos = dualServicos;
	}

	public DualListModel<Produto> getDualProdutos() {
		return dualProdutos;
	}

	public void setDualProdutos(DualListModel<Produto> dualProdutos) {
		this.dualProdutos = dualProdutos;
	}

	public Cliente getClienteNovaComanda() {
		return clienteNovaComanda;
	}

	public void setClienteNovaComanda(Cliente clienteNovaComanda) {
		this.clienteNovaComanda = clienteNovaComanda;
	}

	public boolean isBloquearListasComandas() {
		return bloquearListasComandas;
	}

	public void setBloquearListasComandas(boolean bloquearListasComandas) {
		this.bloquearListasComandas = bloquearListasComandas;
	}

	public AgendaServico getAgendaServico() {
		return agendaServico;
	}

	public void setAgendaServico(AgendaServico agendaServico) {
		this.agendaServico = agendaServico;
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public Float getEntrada() {
		return entrada;
	}

	public void setEntrada(Float entrada) {
		this.entrada = entrada;
	}

	public StatusManagedBean getStatusManagedBean() {
		return statusManagedBean;
	}

	public void setStatusManagedBean(StatusManagedBean statusManagedBean) {
		this.statusManagedBean = statusManagedBean;
	}	
}
