package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Bandeira;
import br.com.salao.servico.BandeiraServico;

@ManagedBean(name="bandeiraMB")
@ViewScoped
public class BandeiraManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Bandeira bandeira;
	private Bandeira bandeiraEditar;
	private List<Bandeira> bandeiras;	
	private String ativo;
	@ManagedProperty(name="bandeiraServico",value="#{bandeiraServico}")
	private BandeiraServico bandeiraServico;
	private boolean bloquearExcluir;
	
	public BandeiraManagedBean(){
		bandeira = new Bandeira();
		bandeiraEditar = new Bandeira();
		bandeiras = new ArrayList<Bandeira>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			bandeiraServico.salvar(bandeira);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Bandeira cadastrada com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		bandeira = new Bandeira();		
	}
	
	public void alterar(){
		try {
			bandeiraServico.alterar(bandeiraEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Bandeira alterada com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		bandeiraEditar = new Bandeira();
	}
	
	public void excluir(){
		try {
			bandeiraServico.excluir(bandeiraEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Bandeira excluída com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		bandeiraEditar = new Bandeira();
	}
	
	public void listar(){
		try {
			bandeiras = null;
			if(ativo.equals("1")){
				bandeiras = bandeiraServico.listar(true);
				bloquearExcluir = false;
			}else{
				bandeiras = bandeiraServico.listar(false);
				bloquearExcluir = false;
			}			
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Bandeira getBandeira() {
		return bandeira;
	}

	public void setBandeira(Bandeira bandeira) {
		this.bandeira = bandeira;
	}

	public Bandeira getBandeiraEditar() {
		return bandeiraEditar;
	}

	public void setBandeiraEditar(Bandeira bandeiraEditar) {
		this.bandeiraEditar = bandeiraEditar;
	}

	public List<Bandeira> getProfissionais() {
		return bandeiras;
	}

	public void setProfissionais(List<Bandeira> bandeiras) {
		this.bandeiras = bandeiras;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public BandeiraServico getBandeiraServico() {
		return bandeiraServico;
	}

	public void setBandeiraServico(BandeiraServico bandeiraServico) {
		this.bandeiraServico = bandeiraServico;
	}

	public boolean getBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
