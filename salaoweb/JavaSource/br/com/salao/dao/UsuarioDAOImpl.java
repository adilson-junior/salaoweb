package br.com.salao.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Usuario;

@Repository("usuarioDAO")
public class UsuarioDAOImpl implements UsuarioDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager em;

	@Override
	public Usuario buscar(String nomeLogin, String senha) {
		StringBuilder sql = new StringBuilder("select usuario from Usuario usuario ");
		sql.append("where usuario.nomeLogin = '").append(nomeLogin).append("' ");
		sql.append("and usuario.senha = '").append(senha).append("'");
		Query query = em.createQuery(sql.toString());
		Usuario usuario = null;
		try{
			usuario = (Usuario) query.getSingleResult();
		} catch (EntityNotFoundException e) {
			//
		} catch (NoResultException e) {
			//
		}	
		return usuario;
	}

	@Override
	public List<Usuario> listar(boolean ativo) {
		StringBuilder sql = new StringBuilder("from Usuario ");
		sql.append("where ativo = ").append(ativo).append(" ");
		sql.append("order by nome");
		Query query = em.createQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Usuario> clientes = query.getResultList();				
		return clientes;
	}

	@Override
	public void salvar(Usuario usuario) {
		em.persist(usuario);		
	}

	@Override
	public void alterar(Usuario usuario) {
		em.merge(usuario);		
	}

	@Override
	public void excluir(Usuario usuario) {
		em.remove(usuario);		
	}

	@Override
	public Long buscarQuantidade(String nomeLogin) {
		StringBuilder sql = new StringBuilder("select count(usuario) from Usuario usuario ");
		sql.append("where usuario.nomeLogin = '").append(nomeLogin).append("' ");		
		Query query = em.createQuery(sql.toString());
		return (Long) query.getSingleResult();		
	}
}
