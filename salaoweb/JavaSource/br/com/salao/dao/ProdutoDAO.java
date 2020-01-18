package br.com.salao.dao;

import java.util.List;

import br.com.salao.modelo.Produto;

public interface ProdutoDAO {
	List<Produto> listar(boolean ativo);
	List<Produto> buscar(String nome);
	Produto buscarPorNome(String nome);
	Produto buscar(int id);
	void salvar(Produto produto);
	void alterar(Produto produto);
	void excluir(Produto produto);
	long buscarQuantidadeServicos(int id);
}
