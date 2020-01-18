package br.com.salao.dao;

import java.util.List;

import br.com.salao.modelo.Categoria;

public interface CategoriaDAO {
	List<Categoria> listar(boolean ativo);
	Categoria buscar(String nome);
	Categoria buscar(int id);
	void salvar(Categoria categoria);
	void alterar(Categoria categoria);
	void excluir(Categoria categoria);
	long buscarQuantidadeProdutosServicos(int id);
}
