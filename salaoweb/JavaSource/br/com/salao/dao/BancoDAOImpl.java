package br.com.salao.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Banco;
import br.com.salao.modelo.Cheque;

@Repository("bancoDAO")
public class BancoDAOImpl implements BancoDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public BancoDAOImpl(){		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banco> listar(boolean ativo) {
		StringBuilder sb = new StringBuilder("from Banco ");
		sb.append("where ativo = ").append(ativo).append(" ");
		sb.append("order by nome");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Banco banco) {
		em.persist(banco);		
	}

	@Override
	public void alterar(Banco banco) {
		em.merge(banco);
	}

	@Override
	public void excluir(Banco banco) {
		em.remove(banco);	
	}

	@Override
	public Banco buscar(String nome) {
		StringBuilder sql = new StringBuilder("from Banco ");
		sql.append("where LOWER(nome) = '").append(nome.toLowerCase()).append("' ");	
		Query query = em.createQuery(sql.toString());
		Banco banco = null;
		try{
			banco = (Banco) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return banco;
	}

	@Override
	public long buscarQuantidadeCheques(int id) {
		StringBuilder sql = new StringBuilder("select count(cheque) from ChequeVO cheque ");
		sql.append("where cheque.banco.id = ").append(id).append(" ");		
		Query query = em.createQuery(sql.toString());		
		return (Long) query.getSingleResult();
	}

	@Override
	public Banco buscar(int id) {
		StringBuilder sql = new StringBuilder("from Banco ");
		sql.append("where id = ").append(id).append(" ");	
		Query query = em.createQuery(sql.toString());
		Banco banco = null;
		try{
			banco = (Banco) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e) {
			return null;
		}
		return banco;
	}

	@Override
	public void salvarCheque(Cheque cheque) {
		em.persist(cheque);
	}
}
