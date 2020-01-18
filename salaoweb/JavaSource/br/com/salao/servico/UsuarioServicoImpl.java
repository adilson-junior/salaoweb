package br.com.salao.servico;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.UsuarioDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Usuario;
import br.com.salao.util.StringUtil;

@Service("usuarioServico")
public class UsuarioServicoImpl implements UsuarioServico , Serializable {	
	
	private static final long serialVersionUID = 1L;
	private static Logger logg = Logger.getLogger(UsuarioServicoImpl.class);
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Transactional(readOnly=true)
	@Override
	public Usuario buscar(String nomeLogin, String senha) throws ServicoException {
		Usuario usuario = null;
		try{
			usuario = usuarioDAO.buscar(nomeLogin, senha);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}		
		if(usuario == null){			
			throw new ServicoException("Usuário ou senha inválidos");
		}
		return usuario;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Usuario> listar(boolean ativo) throws ServicoException {
		List<Usuario> usuarios = null;
		try {
			usuarios = usuarioDAO.listar(ativo);		
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}
		return usuarios;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Usuario usuario) throws ServicoException {
		usuario.setNomeLogin(usuario.getNomeLogin().trim().toLowerCase());		
		long qtd = usuarioDAO.buscarQuantidade(usuario.getNomeLogin());
		if(usuario.getSenha().length() < 6){
			throw new ServicoException("A senha deve ter no mínimo 6 caracteres");
		}
		if(qtd == 0){
			StringUtil stringUtil = new StringUtil();
			String nome = stringUtil.formatarNome(usuario.getNome().trim());
			usuario.setNome(nome);
			usuario.setSenha(usuario.getSenha().trim());
			try {
				usuarioDAO.salvar(usuario);
			} catch (Exception e) {
				logg.error(e);
				throw new ServicoException();
			}
		}else{
			throw new ServicoException("O nome de login '"+usuario.getNomeLogin()+"' já foi cadastrado");
		}
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void alterar(Usuario usuario) throws ServicoException {
		if(usuario.getSenha().length() < 6){
			throw new ServicoException("A senha deve ter no mínimo 6 caracteres");
		}
		StringUtil stringUtil = new StringUtil();
		String nome = stringUtil.formatarNome(usuario.getNome().trim());
		usuario.setNome(nome);
		usuario.setSenha(usuario.getSenha().trim());
		try {
			usuarioDAO.alterar(usuario);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}		
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Usuario usuario) throws ServicoException {
		try {
			usuario.setAtivo(false);
			usuarioDAO.alterar(usuario);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}
	}

}
