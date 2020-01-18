package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Categoria;
import br.com.salao.modelo.Produto;
import br.com.salao.servico.CategoriaServico;
import br.com.salao.servico.ProdutoServico;

@ManagedBean(name="produtoMB")
@ViewScoped
public class ProdutoManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Produto produto;
	private Produto produtoEditar;
	private List<Produto> produtos;	
	private List<Categoria> categorias;
	private String ativo;
	@ManagedProperty(name="produtoServico",value="#{produtoServico}")
	private ProdutoServico produtoServico;
	@ManagedProperty(name="categoriaServico",value="#{categoriaServico}")
	private CategoriaServico categoriaServico;
	private boolean bloquearExcluir;
	
	public ProdutoManagedBean(){
		produto = new Produto();
		produtoEditar = new Produto();
		produtos = new ArrayList<Produto>();	
		categorias = new ArrayList<Categoria>();
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
		listaCategorias();
	}
	
	public void salvar(){
		try {
			produtoServico.salvar(produto);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Produto cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		produto = new Produto();		
	}
	
	public void alterar(){
		try {
			produtoServico.alterar(produtoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Produto alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		produtoEditar = new Produto();
	}
	
	public void excluir(){
		try {
			produtoServico.excluir(produtoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Produto excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		produtoEditar = new Produto();
	}
	
	public void listar(){
		try {
			produtos = null;
			if(ativo.equals("1")){
				produtos = produtoServico.listar(true);
				bloquearExcluir = false;
			}else{
				produtos = produtoServico.listar(false);
				bloquearExcluir = true;
			}			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void listaCategorias(){
		categorias = null;
		try {
			categorias = categoriaServico.listar(true);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Produto getProdutoEditar() {
		return produtoEditar;
	}

	public void setProdutoEditar(Produto produtoEditar) {
		this.produtoEditar = produtoEditar;
	}

	public List<Produto> getProfissionais() {
		return produtos;
	}

	public void setProfissionais(List<Produto> produtos) {
		this.produtos = produtos;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	public CategoriaServico getCategoriaServico() {
		return categoriaServico;
	}

	public void setCategoriaServico(CategoriaServico categoriaServico) {
		this.categoriaServico = categoriaServico;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public boolean isBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
