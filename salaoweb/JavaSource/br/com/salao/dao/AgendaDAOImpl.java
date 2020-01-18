package br.com.salao.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Agenda;

@Repository("agendaDAO")
public class AgendaDAOImpl implements AgendaDAO , Serializable {	
	
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager em;

	public AgendaDAOImpl() {		
	}

	@Override
	public Agenda salvar(Agenda agenda) {
		agenda = em.merge(agenda);
		em.flush();
		return agenda;
	}

	@Override
	public void atualizar(Agenda agenda) {
		em.merge(agenda);
	}

	@Override
	public void excluir(Agenda agenda) {
		em.remove(agenda);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscar(Date dataInicio, Date dataFim) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data between :inicio and :fim ");				
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", dataInicio, TemporalType.DATE);		
		query.setParameter("fim", dataFim, TemporalType.DATE);	
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarAgendaProfissional(Date dataInicio, Date dataFim, Integer idProfissional) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data between :inicio and :fim ");		
		sql.append("and agenda.profissional.id = ").append(idProfissional).append(" ");
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", dataInicio, TemporalType.DATE);		
		query.setParameter("fim", dataFim, TemporalType.DATE);	
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarAgendaCliente(Date dataInicio, Date dataFim, Integer idCliente) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data between :inicio and :fim ");	
		sql.append("and agenda.cliente.id = ").append(idCliente).append(" ");
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", dataInicio, TemporalType.DATE);		
		query.setParameter("fim", dataFim, TemporalType.DATE);	
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarAgendaProfissional(Date data, Integer idProfissional) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data = :data ");		
		sql.append("and agenda.profissional.id = ").append(idProfissional).append(" ");
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", data, TemporalType.DATE);			
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarAgendaCliente(Date data, Integer idCliente) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data = :data ");	
		sql.append("and agenda.cliente.id = ").append(idCliente).append(" ");
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", data, TemporalType.DATE);			
		return query.getResultList();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarQunatidade(Date data, Date horaInicio, Date horaFim, Integer idProfissional) {
		StringBuilder sql = new StringBuilder("from Agenda ag ");
		sql.append("left outer join fetch ag.cliente ");
		sql.append("where ag.data = :data ");
		sql.append("and ag.profissional.id = ").append(idProfissional).append(" ");
		sql.append("and (ag.horaDe = :horaInicio or ag.horaDe =:horaFim)");
		Query query = em.createQuery(sql.toString());		
		query.setParameter("data", data, TemporalType.DATE);
		query.setParameter("horaInicio", horaInicio, TemporalType.TIME);		
		query.setParameter("horaFim", horaFim, TemporalType.TIME);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscar(Date data, boolean fechada) {
		StringBuilder sql = new StringBuilder("select distinct agenda from Agenda agenda ");		
		sql.append("left outer join fetch agenda.cliente ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("where agenda.data = :data ");	
		sql.append("and agenda.fechada =  ").append(fechada).append(" ");
		sql.append("order by agenda.data");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", data, TemporalType.DATE);			
		return query.getResultList();		
	}

	@Override
	public Agenda buscar(int id) {
		StringBuilder sql = new StringBuilder("from Agenda agenda ");		
		sql.append("left outer join fetch agenda.servicos ");		
		sql.append("where agenda.id = ").append(id);				
		Query query = em.createQuery(sql.toString());		
		return (Agenda) query.getSingleResult();
	}

	@Override
	public Long buscarQunatidade(Date data) {
		StringBuilder sql = new StringBuilder("select count(ag)from Agenda ag ");		
		sql.append("where ag.data = :data ");		
		Query query = em.createQuery(sql.toString());	
		query.setParameter("data", data, TemporalType.DATE);	
		return (Long) query.getSingleResult();
	}
}
