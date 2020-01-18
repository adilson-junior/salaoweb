package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Servico;

public interface ServicoServico {
	
	Servico buscar(Integer id) throws ServicoException;
	List<Servico> buscar(String nome) throws ServicoException;	
	List<Servico> listar(boolean ativo) throws ServicoException;
	void salvar(Servico servico) throws ServicoException;
	void alterar(Servico servico) throws ServicoException;
	void excluir(Servico servico) throws ServicoException;	
}
