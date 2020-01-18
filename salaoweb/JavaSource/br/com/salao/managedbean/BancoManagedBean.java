package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Banco;
import br.com.salao.servico.BancoServico;

@ManagedBean(name="bancoMB")
@ViewScoped
public class BancoManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Banco banco;
	private Banco bancoEditar;
	private List<Banco> bancos;	
	private String ativo;
	@ManagedProperty(name="bancoServico",value="#{bancoServico}")
	private BancoServico bancoServico;
	private boolean bloquearExcluir;
	
	public BancoManagedBean(){
		banco = new Banco();
		bancoEditar = new Banco();
		bancos = new ArrayList<Banco>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			bancoServico.salvar(banco);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Banco cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		banco = new Banco();		
	}
	
	public void alterar(){
		try {
			bancoServico.alterar(bancoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Banco alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		bancoEditar = new Banco();
	}
	
	public void excluir(){
		try {
			bancoServico.excluir(bancoEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Banco excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		bancoEditar = new Banco();
	}
	
	public void listar(){
		try {
			bancos = null;
			if(ativo.equals("1")){
				bancos = bancoServico.listar(true);
				bloquearExcluir = false;
			}else{
				bancos = bancoServico.listar(false);
				bloquearExcluir = true;
			}			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Banco getBancoEditar() {
		return bancoEditar;
	}

	public void setBancoEditar(Banco bancoEditar) {
		this.bancoEditar = bancoEditar;
	}

	public List<Banco> getProfissionais() {
		return bancos;
	}

	public void setProfissionais(List<Banco> bancos) {
		this.bancos = bancos;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public BancoServico getBancoServico() {
		return bancoServico;
	}

	public void setBancoServico(BancoServico bancoServico) {
		this.bancoServico = bancoServico;
	}

	public boolean getBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
