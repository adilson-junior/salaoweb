package br.com.salao.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.ClienteDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;
import br.com.salao.util.DateUtil;
import br.com.salao.util.StringUtil;

@Service("clienteServico")
public class ClienteServicoImpl implements ClienteServico, Serializable {	
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private ClienteDAO clienteDAO;
	private static Logger logg = Logger.getLogger(ClienteServicoImpl.class);
	
	public ClienteServicoImpl(){		
	}
	
	/**
	 * Busca um cliente
	 */
	@Transactional(readOnly=true)
	@Override
	public Cliente buscar(Integer id) throws ServicoException {		
		Cliente cliente;
		try {
			cliente = clienteDAO.buscar(id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
		return cliente;
	}

	/**
	 * Lista todos os clientes ativos se ativo = true
	 */
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> listar(boolean ativo) throws ServicoException {		
		List<Cliente> clientes = null;
		try {
			clientes = clienteDAO.listar(ativo);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return clientes;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Cliente cliente) throws ServicoException {	
		if((cliente.getTelefone() == null || cliente.getTelefone().trim().equals(""))
				&& (cliente.getCelular() == null || cliente.getCelular().trim().equals(""))
				|| (cliente.getNome() == null || cliente.getNome().trim().equals(""))){
			logg.error("Nome e telefone ou celeluar devem ser informados.");
			throw new ServicoException("Nome e telefone ou celeluar devem ser informados."); 
		}
		try {				
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(cliente.getNome().trim());			
			cliente.setNome(nome);		
			if(cliente.getEmail() != null){
				cliente.setEmail(cliente.getEmail().toLowerCase().trim());
			}
			clienteDAO.salvar(cliente);			
		} catch (Exception e) {					
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Cliente cliente) throws ServicoException {
		if((cliente.getTelefone() == null || cliente.getTelefone().trim().equals(""))
				&& (cliente.getCelular() == null || cliente.getCelular().trim().equals(""))
				|| (cliente.getNome() == null || cliente.getNome().trim().equals(""))){
			logg.error("Nome e telefone ou celeluar devem ser informados.");
			throw new ServicoException("Nome e telefone ou celeluar devem ser informados."); 
		}
		try{		
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(cliente.getNome().trim());			
			cliente.setNome(nome);	
			if(cliente.getEmail() != null){
				cliente.setEmail(cliente.getEmail().toLowerCase().trim());
			}
			clienteDAO.alterar(cliente);		
		}catch (Exception e) {			
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Cliente cliente) throws ServicoException {
		try{
			DateUtil dateUtil = new DateUtil();			
			long agendasFuturo = clienteDAO.buscarQuantidadeAgendas(cliente.getId(), dateUtil.getDataHora(0, 0, 0));
			if(agendasFuturo > 0){
				logg.error("O cliente "+cliente.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
				throw new ServicoException("O cliente "+cliente.getNome()+" não pode ser excluído, o mesmo tem agenda(s) marcada(s).");
			}
			long agendasTotal = clienteDAO.buscarQuantidadeAgendas(cliente.getId());
			if(agendasTotal > 0){
				// Clientes com histórico de agendas não serão excluídos
				cliente.setAtivo(false);				
				clienteDAO.alterar(cliente);
			}else{
				cliente = clienteDAO.buscar(cliente.getId());
				clienteDAO.exlcuir(cliente);
			}					
		}catch(Exception e){			
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}	

	/**
	 * Busca uma lista de clientes, ativos, com a palavra chave.<br/>
	 * palavraChave - pesquisa por telefone, celular e nome
	 */
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> buscar(String palavraChave) throws ServicoException {
		if(palavraChave == null){
			logg.error("Informe um valor para a pesquisa.");
			throw new ServicoException("Informe um valor para a pesquisa.");
		}
		List<Cliente> clientes = null;
		try {
			clientes = clienteDAO.buscar(palavraChave);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return clientes;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscarAgendas(Integer idCliente) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = clienteDAO.buscarAgendas(idCliente);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return agendas;
	}

	@Transactional(readOnly=true)
	@Override
	public Cliente buscarClienteEAgendas(Date data, Integer id) throws ServicoException {
		Cliente cliente = null;
		try {
			cliente = clienteDAO.buscarClienteEAgendas(data, id);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return cliente;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Cliente> listar(int idCliente, int idClienteEncaixe) throws ServicoException {
		List<Cliente> clientes = null;
		try {
			clientes = clienteDAO.listar(idCliente, idClienteEncaixe);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return clientes;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Cliente> buscarClientesEAgendas(Date data) throws ServicoException {
		List<Cliente> clientes = null;
		try {
			clientes = clienteDAO.buscarClientesEAgendas(data);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return clientes;
	}
}
