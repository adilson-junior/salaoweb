package br.com.salao.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Servico;

@Repository("servicoDAO")
public class ServicoDAOImpl implements ServicoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager em;
	
	public ServicoDAOImpl(){		
	}

	@Override
	public Servico buscar(Integer id) {
		StringBuilder sql = new StringBuilder("from Servico sv ");
		sql.append("left outer join fetch sv.categoria ");
		sql.append("where sv.id = ").append(id).append(" ");		
		Query query = em.createQuery(sql.toString());		
		return (Servico) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Servico> listar(boolean ativo) {
		StringBuilder sql = new StringBuilder("from Servico sv ");
		sql.append("left outer join fetch sv.categoria ");
		sql.append("where sv.ativo = ").append(ativo).append(" ");
		sql.append("order by sv.nome");
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Servico servico) {
		em.persist(servico);
	}

	@Override
	public void alterar(Servico servico) {
		em.merge(servico);
	}

	@Override
	public void excluir(Servico servico) {
		em.remove(servico);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Servico> buscar(String nome) {
		StringBuilder sql = new StringBuilder("from Servico sv ");
		sql.append("left outer join fetch sv.categoria ");
		sql.append("where LOWER(sv.nome) like '%").append(nome.toLowerCase()).append("%' ");		
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}
	
	@Override
	public int buscarQuantidadeProfissionais(Integer idServico) {
		int qtd = 0;
		return qtd;
	}
	
	@Override
	public Long buscarQuantidadeAgendas(Integer idServico) {
		StringBuilder sql = new StringBuilder("from Servico sv ");		
		sql.append("left outer join fetch sv.agendas ");
		sql.append("where sv.id = ").append(idServico);
		Query query = em.createQuery(sql.toString());
		long qtd = 0l;
		try{
			Servico servico = (Servico) query.getSingleResult();
			qtd = (long) servico.getAgendas().size();
		} catch (EntityNotFoundException e) {
			// 0
		} catch (NoResultException e) {
			// 0
		}		
		return qtd;
	}

	@Override
	public Long buscarQuantidadeAgendas(Integer id, Date inicio) {
		StringBuilder sql = new StringBuilder("from Servico sv ");		
		sql.append("left outer join fetch sv.agendas ag ");
		sql.append("where sv.id = ").append(id).append(" ");
		sql.append("and ag.data >= :inicio");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);	
		long qtd = 0l;
		try{		
			Servico servico = (Servico) query.getSingleResult();
			qtd = (long) servico.getAgendas().size();
		} catch (EntityNotFoundException e) {
			// 0
		} catch (NoResultException e) {
			// 0
		}		
		return qtd;
	}
}
