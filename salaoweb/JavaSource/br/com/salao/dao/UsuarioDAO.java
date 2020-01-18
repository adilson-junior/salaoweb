package br.com.salao.dao;

import java.util.List;

import br.com.salao.modelo.Usuario;

public interface UsuarioDAO {
	Usuario buscar(String nomeLogin, String senha);
	List<Usuario> listar(boolean ativo);
	void salvar(Usuario usuario);
	void alterar(Usuario usuario);
	void excluir(Usuario usuario);
	Long buscarQuantidade(String nomeLogin);
}
