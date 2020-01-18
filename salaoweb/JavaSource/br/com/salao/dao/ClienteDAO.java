package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;

public interface ClienteDAO {
	
	Cliente buscar(Integer id);
	Cliente buscarClienteEAgendas(Date data, Integer id);
	List<Cliente> listar(boolean ativo);
	void salvar(Cliente cliente);
	void alterar(Cliente cliente);
	void exlcuir(Cliente cliente);
	List<Cliente> buscar(String palavraChave);
	Long buscarQuantidadeAgendas(Integer idCliente);
	Long buscarQuantidadeAgendas(Integer idCliente, Date inicio);
	Long buscarAgendas(Integer idCliente, Date inicio, Date fim);
	List<Cliente> buscarClientesEAgendas(Date data);
	List<Agenda> buscarAgendas(Integer idCliente);
	List<Cliente> listar(int idCliente, int idClienteEncaixe);
}
