package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Usuario;
import br.com.salao.servico.UsuarioServico;

@ManagedBean(name="usuarioMB")
@ViewScoped
public class UsuarioManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
	private Usuario usuarioEditar;
	private List<Usuario> usuarios;	
	private String ativo;
	@ManagedProperty(name="usuarioServico",value="#{usuarioServico}")
	private UsuarioServico usuarioServico;
	private boolean bloquearExcluir;
	
	public UsuarioManagedBean(){
		usuario = new Usuario();
		usuarioEditar = new Usuario();
		usuarios = new ArrayList<Usuario>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			usuarioServico.salvar(usuario);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Usuário cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		usuario = new Usuario();		
	}
	
	public void alterar(){
		try {
			usuarioServico.alterar(usuarioEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Usuário alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		usuarioEditar = new Usuario();
	}
	
	public void excluir(){
		try {
			usuarioServico.excluir(usuarioEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Usuario excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		usuarioEditar = new Usuario();
	}
	
	public void listar(){
		try {
			usuarios = null;
			if(ativo.equals("1")){
				usuarios = usuarioServico.listar(true);
				bloquearExcluir = false;
			}else{
				usuarios = usuarioServico.listar(false);
				bloquearExcluir = true;
			}
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioEditar() {
		return usuarioEditar;
	}

	public void setUsuarioEditar(Usuario usuarioEditar) {
		this.usuarioEditar = usuarioEditar;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public UsuarioServico getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(UsuarioServico usuarioServico) {
		this.usuarioServico = usuarioServico;
	}

	public boolean isBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
