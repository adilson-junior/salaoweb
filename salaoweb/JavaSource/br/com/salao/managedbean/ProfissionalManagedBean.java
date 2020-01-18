package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Profissional;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name="profissionalMB")
@ViewScoped
public class ProfissionalManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Profissional profissional;
	private Profissional profissionalEditar;
	private List<Profissional> profissionais;	
	private String ativo;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	private boolean bloquearExcluir;

	public ProfissionalManagedBean(){
		profissional = new Profissional();
		profissionalEditar = new Profissional();
		profissionais = new ArrayList<Profissional>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			profissionalServico.salvar(profissional);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Profissional cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		profissional = new Profissional();		
	}
	
	public void alterar(){
		try {
			profissionalServico.alterar(profissionalEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Profissional alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		profissionalEditar = new Profissional();
	}
	
	public void excluir(){
		try {
			profissionalServico.excluir(profissionalEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Profissional excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		profissionalEditar = new Profissional();
	}
	
	public void listar(){
		try {
			profissionais = null;
			if(ativo.equals("1")){
				profissionais = profissionalServico.listar(true);
				bloquearExcluir = false;
			}else{
				profissionais = profissionalServico.listar(false);
				bloquearExcluir = true;
			}			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Profissional getProfissionalEditar() {
		return profissionalEditar;
	}

	public void setProfissionalEditar(Profissional profissionalEditar) {
		this.profissionalEditar = profissionalEditar;
	}

	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public boolean isBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
