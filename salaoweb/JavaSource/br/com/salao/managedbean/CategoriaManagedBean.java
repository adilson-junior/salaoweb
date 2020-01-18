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
import br.com.salao.servico.CategoriaServico;

@ManagedBean(name="categoriaMB")
@ViewScoped
public class CategoriaManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Categoria categoria;
	private Categoria categoriaEditar;
	private List<Categoria> categorias;	
	private String ativo;
	@ManagedProperty(name="categoriaServico",value="#{categoriaServico}")
	private CategoriaServico categoriaServico;
	private boolean bloquearExcluir;
	
	public CategoriaManagedBean(){
		categoria = new Categoria();
		categoriaEditar = new Categoria();
		categorias = new ArrayList<Categoria>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			categoriaServico.salvar(categoria);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Categoria cadastrada com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		categoria = new Categoria();		
	}
	
	public void alterar(){
		try {
			categoriaServico.alterar(categoriaEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Categoria alterada com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		categoriaEditar = new Categoria();
	}
	
	public void excluir(){
		try {
			categoriaServico.excluir(categoriaEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Categoria excluída com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		categoriaEditar = new Categoria();
	}
	
	public void listar(){
		try {
			categorias = null;
			if(ativo.equals("1")){
				categorias = categoriaServico.listar(true);
				bloquearExcluir = false;
			}else{
				categorias = categoriaServico.listar(false);
				bloquearExcluir = true;
			}			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoriaEditar() {
		return categoriaEditar;
	}

	public void setCategoriaEditar(Categoria categoriaEditar) {
		this.categoriaEditar = categoriaEditar;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public CategoriaServico getCategoriaServico() {
		return categoriaServico;
	}

	public void setCategoriaServico(CategoriaServico categoriaServico) {
		this.categoriaServico = categoriaServico;
	}

	public boolean isBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
