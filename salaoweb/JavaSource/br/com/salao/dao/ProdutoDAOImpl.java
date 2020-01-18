package br.com.salao.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Produto;
@Repository("produtoDAO")
public class ProdutoDAOImpl implements ProdutoDAO, Serializable {
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public ProdutoDAOImpl(){		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> listar(boolean ativo) {
		StringBuilder sb = new StringBuilder("from Produto p ");
		sb.append("left outer join fetch p.categoria ");
		sb.append("where p.ativo = ").append(ativo).append(" ");
		sb.append("order by p.nome");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Produto produto) {
		em.persist(produto);		
	}

	@Override
	public void alterar(Produto produto) {
		em.merge(produto);
	}

	@Override
	public void excluir(Produto produto) {
		em.remove(produto);	
	}

	@Override
	public Produto buscarPorNome(String nome) {
		StringBuilder sql = new StringBuilder("from Produto ");
		sql.append("where LOWER(nome) = '").append(nome.toLowerCase()).append("' ");	
		Query query = em.createQuery(sql.toString());
		Produto produto = null;
		try{
			produto = (Produto) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return produto;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> buscar(String nome) {
		StringBuilder sql = new StringBuilder("from Produto p ");
		sql.append("left outer join fetch p.categoria ");
		sql.append("where LOWER(p.nome) like '%").append(nome.toLowerCase()).append("%' ");	
		Query query = em.createQuery(sql.toString());	
		return query.getResultList();
	}

	@Override
	public long buscarQuantidadeServicos(int id) {
		StringBuilder sql = new StringBuilder("select count(item) from Item item ");
		sql.append("where item.produto.id = ").append(id).append(" ");		
		Query query = em.createQuery(sql.toString());		
		return (Long) query.getSingleResult();
	}

	@Override
	public Produto buscar(int id) {
		StringBuilder sql = new StringBuilder("from Produto p ");
		sql.append("left outer join fetch p.categoria ");
		sql.append("where p.id = ").append(id).append(" ");	
		Query query = em.createQuery(sql.toString());
		Produto produto = null;
		try{
			produto = (Produto) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return produto;
	}

}
