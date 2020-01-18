package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Bandeira;

public interface BandeiraServico {
	List<Bandeira> listar(boolean ativo) throws ServicoException;
	void salvar(Bandeira bandeira) throws ServicoException;
	void alterar(Bandeira bandeira) throws ServicoException;
	void excluir(Bandeira bandeira) throws ServicoException;
	Bandeira buscar(int id) throws ServicoException;
}
