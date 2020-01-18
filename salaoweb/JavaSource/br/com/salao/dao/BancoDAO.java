package br.com.salao.dao;

import java.util.List;

import br.com.salao.modelo.Banco;
import br.com.salao.modelo.Cheque;

public interface BancoDAO {
	List<Banco> listar(boolean ativo);
	Banco buscar(String nome);
	Banco buscar(int id);
	void salvar(Banco banco);
	void alterar(Banco banco);
	void excluir(Banco banco);
	long buscarQuantidadeCheques(int id);
	void salvarCheque(Cheque cheque);
}
