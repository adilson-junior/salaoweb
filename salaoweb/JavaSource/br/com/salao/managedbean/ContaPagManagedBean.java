package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.ContaPag;
import br.com.salao.servico.ContaPagServico;

@ManagedBean(name="contaPagMB")
@ViewScoped
public class ContaPagManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private ContaPag contaPag;
	private ContaPag contaPagEditar;
	private List<ContaPag> contasPag;
	private Date dataInicio;
	@ManagedProperty(name="contaPagServico",value="#{contaPagServico}")
	private ContaPagServico contaPagServico;
	private String total;
	private String tipoPesquisa;
	
	public ContaPagManagedBean(){
		contaPag = new ContaPag();
		contaPagEditar = new ContaPag();
		contasPag = new ArrayList<ContaPag>();		
		dataInicio = new Date();
		tipoPesquisa = "R";
	}
	
	@PostConstruct
	public void iniciar(){		
		carregar();
	}
	
	public void salvar(){
		try {
			contaPagServico.salvar(contaPag);
			carregar();
			addMensagem(FacesMessage.SEVERITY_INFO, "Salvar", "Conta cadastrada com sucesso");	
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Salvar", e.getMessage());	
		}
		finally{
			contaPag = new ContaPag();
		}
	}
	
	public void alterar(){
		try {
			contaPagServico.alterar(contaPagEditar);
			carregar();
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração", "Conta alterada com sucesso");	
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alterar", e.getMessage());	
		}
		finally{
			contaPagEditar = new ContaPag();
		}
	}
	
	public void excluir(){
		try {
			contaPagServico.excluir(contaPagEditar);
			carregar();
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluír", "Conta excluída com sucesso");	
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluír", e.getMessage());	
		}
		finally{
			contaPagEditar = new ContaPag();
		}
	}
	
	public void carregar(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataInicio);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date inicio = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		Date fim = cal.getTime();
		try {			
			contasPag = contaPagServico.listar(inicio, fim, tipoPesquisa);
			float total = 0f;
			for(ContaPag ctP : contasPag){
				total += ctP.getValor();
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();			
			this.total = nFormat.format(total);	
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());	
		}
	}
	
	public ContaPag getContaPag() {
		return contaPag;
	}

	public void setContaPag(ContaPag contaPag) {
		this.contaPag = contaPag;
	}

	public ContaPag getContaPagEditar() {
		return contaPagEditar;
	}

	public void setContaPagEditar(ContaPag contaPagEditar) {
		this.contaPagEditar = contaPagEditar;
	}

	public List<ContaPag> getContasPag() {
		return contasPag;
	}

	public void setContasPag(List<ContaPag> contasPag) {
		this.contasPag = contasPag;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public ContaPagServico getContaPagServico() {
		return contaPagServico;
	}

	public void setContaPagServico(ContaPagServico contaPagServico) {
		this.contaPagServico = contaPagServico;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(String tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}
	
}
