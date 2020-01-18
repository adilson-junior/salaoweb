package br.com.salao.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.ContaPag;

@Repository("contaPagDAO")
public class ContaPagDAOImpl implements ContaPagDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public ContaPagDAOImpl(){
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContaPag> listar(Date inicio, Date fim, String colunaPesquisa) {
		String colunaData = "";		
		if(colunaPesquisa.equals("V")){
			colunaData = "dataVencimento";	
		}else if(colunaPesquisa.equals("P")){
			colunaData = "dataPagamento";				
		}else{ // R
			colunaData = "mesAnoReferencia";	
		}
		StringBuilder sql = new StringBuilder("from ContaPag ");
		sql.append("where ").append(colunaData).append(" between :inicio and :fim ");
		sql.append("order by dataVencimento");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", inicio, TemporalType.DATE);
		query.setParameter("fim", fim, TemporalType.DATE);
		return query.getResultList();
	}

	@Override
	public ContaPag buscar(int id) {
		StringBuilder sql = new StringBuilder("from ContaPag ");
		sql.append("where id = ").append(id);		
		Query query = em.createQuery(sql.toString());	
		ContaPag contaPag = null;
		try{
			contaPag = (ContaPag) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e){
			return null;
		}
		return contaPag;
	}

	@Override
	public void salvar(ContaPag contaPag) {
		em.persist(contaPag);
	}

	@Override
	public void alterar(ContaPag contaPag) {
		em.merge(contaPag);
	}

	@Override
	public void excluir(ContaPag contaPag) {
		em.remove(contaPag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContaPag> listarAtrasadas() {		
		StringBuilder sql = new StringBuilder("from ContaPag ");
		sql.append("where dataPagamento = null and dataVencimento < :data ");		
		sql.append("order by dataVencimento");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", new Date(), TemporalType.DATE);		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContaPag> listarAVencer() {		
		Calendar cal = Calendar.getInstance();			
		Date inicio = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 6);
		Date fim = cal.getTime();
		StringBuilder sql = new StringBuilder("from ContaPag ");
		sql.append("where dataPagamento = null ");
		sql.append("and (dataVencimento between :inicio and :fim) ");
		sql.append("order by dataVencimento");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", inicio, TemporalType.DATE);
		query.setParameter("fim", fim, TemporalType.DATE);
		return query.getResultList();
	}
}
