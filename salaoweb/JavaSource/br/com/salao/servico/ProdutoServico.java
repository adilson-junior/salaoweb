package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Produto;

public interface ProdutoServico {
	List<Produto> listar(boolean ativo) throws ServicoException;
	void salvar(Produto produto) throws ServicoException;
	void alterar(Produto produto) throws ServicoException;
	void excluir(Produto produto) throws ServicoException;
	Produto buscar(int id) throws ServicoException;
	List<Produto> buscar(String nome) throws ServicoException;	
}
