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
import br.com.salao.modelo.Servico;
import br.com.salao.servico.CategoriaServico;
import br.com.salao.servico.ServicoServico;

@ManagedBean(name="servicoMB")
@ViewScoped
public class ServicoManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Servico servico;
	private Servico servicoEditar;
	private List<Servico> servicos;	
	private List<Categoria> categorias;
	private String ativo;
	@ManagedProperty(name="servicoServico",value="#{servicoServico}")
	private ServicoServico servicoServico;
	@ManagedProperty(name="categoriaServico",value="#{categoriaServico}")
	private CategoriaServico categoriaServico;
	private boolean bloquearExcluir;
	
	public ServicoManagedBean(){
		servico = new Servico();
		servicoEditar = new Servico();
		servicos = new ArrayList<Servico>();	
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
			servico.setDuracao(0);
			servicoServico.salvar(servico);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Servico cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		servico = new Servico();		
	}
	
	public void alterar(){
		try {
			servicoServico.alterar(servicoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Servico alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		servicoEditar = new Servico();
	}
	
	public void excluir(){
		try {
			servicoServico.excluir(servicoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Servico excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		servicoEditar = new Servico();
	}
	
	public void listar(){
		try {
			servicos = null;
			if(ativo.equals("1")){
				servicos = servicoServico.listar(true);
				bloquearExcluir = false;
			}else{
				servicos = servicoServico.listar(false);
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
	
	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public Servico getServicoEditar() {
		return servicoEditar;
	}

	public void setServicoEditar(Servico servicoEditar) {
		this.servicoEditar = servicoEditar;
	}

	public List<Servico> getProfissionais() {
		return servicos;
	}

	public void setProfissionais(List<Servico> servicos) {
		this.servicos = servicos;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public ServicoServico getServicoServico() {
		return servicoServico;
	}

	public void setServicoServico(ServicoServico servicoServico) {
		this.servicoServico = servicoServico;
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

	public boolean getBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
