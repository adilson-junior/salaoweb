package br.com.salao.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Vale;
import br.com.salao.util.DateUtil;

@Repository("entradaSaidaDAO")
public class EntradaSaidaDAOImpl implements EntradaSaidaDAO , Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public EntradaSaidaDAOImpl(){		
	}

	@Override
	public void salvarReforco(Reforco reforco) {
		em.persist(reforco);
	}

	@Override
	public void alterarReforco(Reforco reforco) {
		em.merge(reforco);
	}

	@Override
	public void excluirReforco(Reforco reforco) {
		em.remove(reforco);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reforco> listarReforcos(Integer idCaixa) {
		StringBuilder sb = new StringBuilder("from Reforco reforco ");		
		sb.append("where reforco.caixa.id = ").append(idCaixa).append(" ");
		sb.append("order by data");
		Query query = em.createQuery(sb.toString());	
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Reforco> listarReforcos(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Reforco reforco ");		
		sb.append("where reforco.data between :inicio and :fim ");			
		sb.append("order by data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();		
	}

	@Override
	public void salvarSangria(Sangria sangria) {
		em.persist(sangria);
	}

	@Override
	public void alterarSangria(Sangria sangria) {
		em.merge(sangria);
	}

	@Override
	public void excluirSangria(Sangria sangria) {
		em.remove(sangria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sangria> listarSangrias(Integer idCaixa) {
		StringBuilder sb = new StringBuilder("from Sangria sangria ");	
		sb.append("where sangria.caixa.id = ").append(idCaixa).append(" ");
		sb.append("order by data");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Sangria> listarSangrias(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Sangria sangria ");	
		sb.append("where sangria.data between :inicio and :fim ");			
		sb.append("order by data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();		
	}

	@Override
	public void salvarVale(Vale vale) {
		em.persist(vale);
	}

	@Override
	public void alterarVale(Vale vale) {
		em.merge(vale);
	}

	@Override
	public void excluirVale(Vale vale) {
		em.remove(vale);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vale> listarVales(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Vale vale ");	
		sb.append("left outer join fetch vale.profissional ");
		sb.append("where vale.data between :inicio and :fim ");			
		sb.append("order by vale.data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vale> listarVales(Integer idProfissional, Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Vale vale ");
		sb.append("left outer join fetch vale.profissional profissional ");
		sb.append("where vale.data between :inicio and :fim ");			
		sb.append("and profissional.id = ").append(idProfissional).append(" ");
		sb.append("order by vale.data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();	
	}

	@Override
	public Vale buscarVale(Integer id) {
		StringBuilder sb = new StringBuilder("from Vale ");		
		sb.append("where id = ").append(id);
		Query query = em.createQuery(sb.toString());
		return (Vale) query.getSingleResult();
	}

	@Override
	public Sangria buscarSangria(Integer id) {
		StringBuilder sb = new StringBuilder("from Sangria ");		
		sb.append("where id = ").append(id);
		Query query = em.createQuery(sb.toString());
		return (Sangria) query.getSingleResult();
	}

	@Override
	public Reforco buscarReforco(Integer id) {
		StringBuilder sb = new StringBuilder("from Reforco ");		
		sb.append("where id = ").append(id);
		Query query = em.createQuery(sb.toString());
		return (Reforco) query.getSingleResult();
	}
	
	@Override
	public void salvarOutros(Outros outros) {
		em.persist(outros);
	}

	@Override
	public void alterarOutros(Outros outros) {
		em.merge(outros);
	}

	@Override
	public void excluirOutros(Outros outros) {
		em.remove(outros);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Outros> listarOutross(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Outros outros ");	
		sb.append("left outer join fetch outros.profissional ");
		sb.append("where outros.data between :inicio and :fim ");			
		sb.append("order by outros.data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Outros> listarOutross(Integer idProfissional, Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Outros outros ");
		sb.append("left outer join fetch outros.profissional profissional ");
		sb.append("where outros.data between :inicio and :fim ");			
		sb.append("and profissional.id = ").append(idProfissional).append(" ");
		sb.append("order by outros.data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();	
	}

	@Override
	public Outros buscarOutros(Integer id) {
		StringBuilder sb = new StringBuilder("from Outros ");		
		sb.append("where id = ").append(id);
		Query query = em.createQuery(sb.toString());
		return (Outros) query.getSingleResult();
	}

}
