package br.com.salao.managedbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Profissional;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name="mloginMB")
@SessionScoped
public class MLoginManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;	
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	private String senha;
	private String telefone;
	private Profissional profissional;
	private String senhaAtual;
	private String novaSenha;
	
	public MLoginManagedBean(){
		senhaAtual = "";
	}
	
	public void login(){
		try {
			StringBuilder tel = new StringBuilder();
			for(int i = 0; i < telefone.length(); i++){
				if(i == 4){
					tel.append(telefone.charAt(i)).append("-");
				}else{
					tel.append(telefone.charAt(i));
				}
			}
			profissional = profissionalServico.buscar(senha, tel.toString());
			if(profissional == null){
				addMensagem(FacesMessage.SEVERITY_ERROR, "Login","Usuário ou senha inválidos");	
			}else{
				getRequest().getSession().setAttribute("profissional", profissional);
				redirecionar("m/pg/index.jsf");
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Login",e.getMessage());	
		}
	}
	
	public void logout(){
		getRequest().getSession().invalidate();
		redirecionar("m/");
	}
	
	public void alterarSenha(){
		if(!senhaAtual.trim().equals(profissional.getSenha())){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alterar senha","Senha atual não confere");
			return;
		}
		if(senhaAtual.trim().equals(novaSenha.trim())){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alterar senha","Não informe a mesma senha");
			return;
		}
		if(novaSenha.length() < 6){			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alterar senha","A senha deve ter no mínimo 6 caracteres");
			return;
		}
		profissional.setSenha(novaSenha.trim());
		try {
			profissionalServico.alterar(profissional);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alterar senha","Senha alterada com sucesso");
			senhaAtual = "";
			novaSenha = "";
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alterar senha",e.getMessage());
		}
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

}
