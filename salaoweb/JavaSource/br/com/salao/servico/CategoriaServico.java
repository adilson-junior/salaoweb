package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Categoria;

public interface CategoriaServico {
	List<Categoria> listar(boolean ativo) throws ServicoException;
	void salvar(Categoria categoria) throws ServicoException;
	void alterar(Categoria categoria) throws ServicoException;
	void excluir(Categoria categoria) throws ServicoException;
	Categoria buscar(int id) throws ServicoException;	
}
