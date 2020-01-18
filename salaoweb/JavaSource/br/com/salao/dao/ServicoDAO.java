package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Servico;

public interface ServicoDAO {

	Servico buscar(Integer id);
	List<Servico> buscar(String nome);	
	List<Servico> listar(boolean ativo);
	Long buscarQuantidadeAgendas(Integer idServico);
	Long buscarQuantidadeAgendas(Integer idServico, Date inicio);
	int buscarQuantidadeProfissionais(Integer idServico);
	void salvar(Servico servico);
	void alterar(Servico servico);
	void excluir(Servico servico);
}
