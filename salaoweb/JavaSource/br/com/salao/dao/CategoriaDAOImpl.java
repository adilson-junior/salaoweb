package br.com.salao.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Categoria;

@Repository("categoriaDAO")
public class CategoriaDAOImpl implements CategoriaDAO, Serializable {
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public CategoriaDAOImpl(){		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> listar(boolean ativo) {
		StringBuilder sb = new StringBuilder("from Categoria ");
		sb.append("where ativo = ").append(ativo).append(" ");
		sb.append("order by nome");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Categoria categoria) {
		em.persist(categoria);		
	}

	@Override
	public void alterar(Categoria categoria) {
		em.merge(categoria);
	}

	@Override
	public void excluir(Categoria categoria) {
		em.remove(categoria);	
	}

	@Override
	public Categoria buscar(String nome) {
		StringBuilder sql = new StringBuilder("from Categoria ");
		sql.append("where LOWER(nome) = '").append(nome.toLowerCase()).append("' ");	
		Query query = em.createQuery(sql.toString());
		Categoria categoria = null;
		try{
			categoria = (Categoria) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return categoria;
	}

	@Override
	public long buscarQuantidadeProdutosServicos(int id) {
		StringBuilder sql1 = new StringBuilder("select count(produto) from Produto produto ");
		sql1.append("where produto.categoria.id = ").append(id).append(" ");		
		Query query = em.createQuery(sql1.toString());		
		long qtd = (Long) query.getSingleResult();
		StringBuilder sql2 = new StringBuilder("select count(servico) from Servico servico ");
		sql2.append("where servico.categoria.id = ").append(id).append(" ");		
		query = em.createQuery(sql2.toString());
		qtd += (Long) query.getSingleResult();
		return qtd;
	}

	@Override
	public Categoria buscar(int id) {
		StringBuilder sql = new StringBuilder("from Categoria ");
		sql.append("where id = ").append(id).append(" ");	
		Query query = em.createQuery(sql.toString());
		Categoria categoria = null;
		try{
			categoria = (Categoria) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return categoria;
	}
}
