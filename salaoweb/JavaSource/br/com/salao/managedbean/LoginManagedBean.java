package br.com.salao.managedbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Usuario;
import br.com.salao.servico.UsuarioServico;

@ManagedBean(name="loginMB")
@SessionScoped
public class LoginManagedBean extends BaseBean{

	private static final long serialVersionUID = 1L;	
	private String nome;
	private String senha;
	@ManagedProperty(name="usuarioServico",value="#{usuarioServico}")
	private UsuarioServico usuarioServico;
	private Usuario usuario;
	
	public LoginManagedBean(){
		nome = "";
		senha = "";
	}
	
	public void validar(){
		try {
			usuario = usuarioServico.buscar(nome, senha);
			HttpSession session = getRequest().getSession();
			session.setAttribute("usuario", usuario);
			if(usuario.getAdmin()){
				session.setAttribute("ehAdmin", true);
			}else{
				session.setAttribute("ehAdmin", false);
			}			
			redirecionar("pg/index.jsf");
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Login", e.getMessage());
		}
	}	
	
	public void sair(){
		getRequest().getSession().invalidate();
		redirecionar("/");
	}

	public UsuarioServico getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(UsuarioServico usuarioServico) {
		this.usuarioServico = usuarioServico;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Usuario getUsuario() {
		return usuario;
	}	
}
