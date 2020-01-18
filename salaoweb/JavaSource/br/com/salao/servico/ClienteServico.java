package br.com.salao.servico;

import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;

public interface ClienteServico {
	Cliente buscar(Integer id) throws ServicoException;	
	List<Cliente> listar(boolean ativo) throws ServicoException;
	List<Cliente> listar(int idCliente, int idClienteEncaixe)throws ServicoException;
	List<Cliente> buscar(String palavraChave) throws ServicoException;
	List<Agenda> buscarAgendas(Integer idCliente) throws ServicoException;
	Cliente buscarClienteEAgendas(Date data, Integer id) throws ServicoException;
	List<Cliente> buscarClientesEAgendas(Date data) throws ServicoException;
	void salvar(Cliente cliente) throws ServicoException;
	void alterar(Cliente cliente) throws ServicoException;
	void excluir(Cliente cliente) throws ServicoException;
}
