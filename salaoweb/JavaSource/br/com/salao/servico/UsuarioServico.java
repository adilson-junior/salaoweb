package br.com.salao.servico;

import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Usuario;

public interface UsuarioServico {
	Usuario buscar(String nomeLogin, String senha) throws ServicoException;
	List<Usuario> listar(boolean ativo) throws ServicoException;
	void salvar(Usuario usuario) throws ServicoException;
	void alterar(Usuario usuario) throws ServicoException;
	void excluir(Usuario usuario) throws ServicoException;
}
