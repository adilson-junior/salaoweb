package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Cliente;
import br.com.salao.servico.ClienteServico;

@ManagedBean(name="clienteMB")
@ViewScoped
public class ClienteManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private Cliente cliente;
	private Cliente clienteEditar;
	private List<Cliente> clientes;	
	private String ativo;
	@ManagedProperty(name="clienteServico",value="#{clienteServico}")
	private ClienteServico clienteServico;
	private boolean bloquearExcluir;
	
	public ClienteManagedBean(){
		cliente = new Cliente();
		clienteEditar = new Cliente();
		clientes = new ArrayList<Cliente>();		
		ativo = "1";
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
	}
	
	public void salvar(){
		try {
			clienteServico.salvar(cliente);
			addMensagem(FacesMessage.SEVERITY_INFO, "Cadastro","Cliente cadastrado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro",e.getMessage());			
		}
		cliente = new Cliente();		
	}
	
	public void alterar(){
		try {
			clienteServico.alterar(clienteEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Alteração","Cliente alterado com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Alteração",e.getMessage());			
		}
		clienteEditar = new Cliente();
	}
	
	public void excluir(){
		try {
			clienteServico.excluir(clienteEditar);
			addMensagem(FacesMessage.SEVERITY_INFO, "Excluir","Cliente excluído com sucesso");
			listar();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Excluir",e.getMessage());			
		}
		clienteEditar = new Cliente();
	}
	
	public void listar(){
		try {
			clientes = null;
			if(ativo.equals("1")){
				clientes = clienteServico.listar(true);
				bloquearExcluir = false;
			}else{
				clientes = clienteServico.listar(false);
				bloquearExcluir = true;
			}
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteEditar() {
		return clienteEditar;
	}

	public void setClienteEditar(Cliente clienteEditar) {
		this.clienteEditar = clienteEditar;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}	

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public ClienteServico getClienteServico() {
		return clienteServico;
	}

	public void setClienteServico(ClienteServico clienteServico) {
		this.clienteServico = clienteServico;
	}

	public boolean isBloquearExcluir() {
		return bloquearExcluir;
	}

	public void setBloquearExcluir(boolean bloquearExcluir) {
		this.bloquearExcluir = bloquearExcluir;
	}
}
