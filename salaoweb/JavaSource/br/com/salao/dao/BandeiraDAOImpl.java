package br.com.salao.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Bandeira;
import br.com.salao.modelo.Cartao;

@Repository("bandeiraDAO")
public class BandeiraDAOImpl implements BandeiraDAO, Serializable {
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public BandeiraDAOImpl(){		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bandeira> listar(boolean ativo) {
		StringBuilder sb = new StringBuilder("from Bandeira ");
		sb.append("where ativo = ").append(ativo).append(" ");
		sb.append("order by nome");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Bandeira bandeira) {
		em.persist(bandeira);		
	}

	@Override
	public void alterar(Bandeira bandeira) {
		em.merge(bandeira);
	}

	@Override
	public void excluir(Bandeira bandeira) {
		em.remove(bandeira);	
	}

	@Override
	public Bandeira buscar(String nome) {
		StringBuilder sql = new StringBuilder("from Bandeira ");
		sql.append("where LOWER(nome) = '").append(nome.toLowerCase()).append("' ");	
		Query query = em.createQuery(sql.toString());
		Bandeira Bandeira = null;
		try{
			Bandeira = (Bandeira) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return Bandeira;
	}

	@Override
	public long buscarQuantidadeCartoes(int id) {
		StringBuilder sql = new StringBuilder("select count(cartao) from Cartao cartao ");
		sql.append("where cartao.bandeira.id = ").append(id).append(" ");		
		Query query = em.createQuery(sql.toString());		
		return (Long) query.getSingleResult();
	}

	@Override
	public Bandeira buscar(int id) {
		StringBuilder sql = new StringBuilder("from Bandeira ");
		sql.append("where id = ").append(id).append(" ");	
		Query query = em.createQuery(sql.toString());
		Bandeira bandeira = null;
		try{
			bandeira = (Bandeira) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return bandeira;
	}

	@Override
	public void salvarCartao(Cartao cartao) {
		em.persist(cartao);
	}
}
