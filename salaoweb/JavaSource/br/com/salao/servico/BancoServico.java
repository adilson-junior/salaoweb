package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Banco;

public interface BancoServico {
	List<Banco> listar(boolean ativo) throws ServicoException;
	void salvar(Banco banco) throws ServicoException;
	void alterar(Banco banco) throws ServicoException;
	void excluir(Banco banco) throws ServicoException;
	Banco buscar(int id) throws ServicoException;
}
