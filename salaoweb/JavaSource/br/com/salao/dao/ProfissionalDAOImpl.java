package br.com.salao.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Item;
import br.com.salao.modelo.Outros;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Vale;
import br.com.salao.util.DateUtil;

@Repository("profissionalDAO")
public class ProfissionalDAOImpl implements ProfissionalDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager em;
	
	public ProfissionalDAOImpl(){		
	}

	@Override
	public Profissional buscar(Integer id) {
		if(id == 0){
			return null;
		}	
		Profissional profissional = null;
		try{
			StringBuilder sql = new StringBuilder("from Profissional p ");			
			sql.append("where p.id = ").append(id);
			Query query = em.createQuery(sql.toString());
			profissional = (Profissional) query.getSingleResult();
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e){
			return null;
		}
		return profissional;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> listar(boolean ativo) {
		StringBuilder sql = new StringBuilder("select distinct p from Profissional p ");		
		sql.append("where p.ativo = ").append(ativo).append(" ");
		sql.append("order by p.nome");
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> listar(boolean ativo, Date dataCadastro) {
		StringBuilder sql = new StringBuilder("select distinct p from Profissional p ");	
		sql.append("left outer join fetch p.vales ");
		sql.append("left outer join fetch p.outros ");
		sql.append("where p.ativo = ").append(ativo).append(" ");	
		sql.append("and p.dataCadastro <= :data ");
		sql.append("order by p.nome");		
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", dataCadastro, TemporalType.DATE);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> listarProfissionalPagamentoComissao() {
		StringBuilder sql = new StringBuilder("select distinct p from Profissional p ");		
		sql.append("where p.salarioFixo = false ");
		sql.append("and p.ativo = true ");
		sql.append("order by p.nome");
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public void salvar(Profissional profissional) {
		em.persist(profissional);
	}

	@Override
	public void alterar(Profissional profissional) {
		em.merge(profissional);
	}

	@Override
	public void excluir(Profissional profissional) {
		em.remove(profissional);
	}

	@Override
	public Long buscarQuantidadeAgendas(Integer idProfissional) {
		StringBuilder sql = new StringBuilder("select count(ag) from Agenda ag ");
		sql.append("where ag.profissional.id = ").append(idProfissional);
		Query query = em.createQuery(sql.toString());
		return (Long) query.getSingleResult();
	}

	@Override
	public Long buscarAgendas(Integer idProfissional, Date inicio, Date fim) {
		
		return null;
	}

	@Override
	public Long buscarQuantidadeAgendas(Integer idProfissional, Date inicio) {
		StringBuilder sql = new StringBuilder("select count(ag) from Agenda ag ");
		sql.append("where ag.profissional.id = ").append(idProfissional).append(" ");
		sql.append("and ag.data >= :inicio");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);		
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> buscar(String palavraChave) {
		StringBuilder sql = new StringBuilder("from Profissional ");
		sql.append("where LOWER(nome) like '%").append(palavraChave.toLowerCase()).append("%' ");
		sql.append("or telefone like '%").append(palavraChave).append("%' ");
		sql.append("or celular like '%").append(palavraChave).append("%' ");
		sql.append("and ativo = true order by nome");
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> buscarProfissionaisEAgendas(boolean ativo, Date data) {
		StringBuilder sql = new StringBuilder("select distinct prof from Profissional prof ");
		sql.append("left outer join fetch prof.agendas agenda ");
		sql.append("left outer join fetch agenda.cliente ");
		sql.append("where prof.ativo = ").append(ativo).append(" ");
		sql.append("and agenda.data = :data ");		
		sql.append("order by agenda.id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", data, TemporalType.DATE);	
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profissional> buscarProfissionaisEItensFaturamento(Date inicio, Date fim, List<Integer> ids) {
		String idsProf = ids.toString().replaceAll("[\\[\\]]","");
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);	
		//Itens
		StringBuilder sql = new StringBuilder("select distinct prof from Profissional prof ");
		sql.append("left outer join fetch prof.itens item ");				
		sql.append("where item.data between :inicio and :fim ");	
		sql.append("and prof.id in (").append(idsProf).append(")");
		Query query = em.createQuery(sql.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Profissional> profissItens = query.getResultList();
		//Vales
		StringBuilder sql1 = new StringBuilder("select distinct prof from Profissional prof ");	
		sql1.append("left outer join fetch prof.vales vale ");	
		sql1.append("where vale.data between :inicio and :fim ");
		sql1.append("and prof.id in (").append(idsProf).append(")");
		query = em.createQuery(sql1.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Profissional> profissVales = query.getResultList();
		//Outros
		StringBuilder sql2 = new StringBuilder("select distinct prof from Profissional prof ");	
		sql2.append("left outer join fetch prof.outros outro ");
		sql2.append("where outro.data between :inicio and :fim ");
		sql2.append("and prof.id in (").append(idsProf).append(")");
		query = em.createQuery(sql2.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Profissional> profissOutros = query.getResultList();	
		Map<Integer, Profissional> mapProf = new HashMap<Integer, Profissional>();
		List<Profissional> profissionais = new ArrayList<Profissional>();
		for(Profissional pf : profissItens){
			Profissional profN = new Profissional();
			profN.setId(pf.getId());
			profN.setNome(pf.getNome());
			profN.setItens(pf.getItens());
			mapProf.put(pf.getId(), profN);			
		}
		for(Profissional pf : profissVales){
			Profissional prof = mapProf.get(pf.getId());			
			if(prof == null){			
				Profissional profN = new Profissional();
				profN.setId(pf.getId());
				profN.setNome(pf.getNome());
				profN.setVales(pf.getVales());
				mapProf.put(pf.getId(), profN);
			}else{
				prof.setVales(pf.getVales());
			}
		}
		for(Profissional pf : profissOutros){
			Profissional prof = mapProf.get(pf.getId());			
			if(prof == null){
				Profissional profN = new Profissional();
				profN.setId(pf.getId());
				profN.setNome(pf.getNome());
				profN.setOutros(pf.getOutros());
				mapProf.put(pf.getId(), profN);
			}else{
				prof.setOutros(pf.getOutros());
			}
		}
		profissionais.addAll(mapProf.values());				
		return profissionais;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Profissional buscarProfissionalEItensFaturamento(Date inicio, Date fim, Integer id) {
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);	
		//Itens
		StringBuilder sql = new StringBuilder("select distinct item from Item item ");	
		//sql.append("left outer join fetch item.profissional ");
		sql.append("where item.data between :inicio and :fim ");
		sql.append("and item.profissional.id = ").append(id);
		Query query = em.createQuery(sql.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Item> itens = query.getResultList();
		//Vales
		StringBuilder sql1 = new StringBuilder("select distinct vale from Vale vale ");			
		sql1.append("where vale.data between :inicio and :fim ");
		sql1.append("and vale.profissional.id = ").append(id);
		query = em.createQuery(sql1.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Vale> vales = query.getResultList();
		//Outros
		StringBuilder sql2 = new StringBuilder("select distinct outro from Outros outro ");
		sql2.append("where outro.data between :inicio and :fim ");
		sql2.append("and outro.profissional.id = ").append(id);
		query = em.createQuery(sql2.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		List<Outros> outros = query.getResultList();			
		Profissional profissional = buscar(id);
		SortedSet<Item> sitens = new TreeSet<Item>();
		SortedSet<Vale> svales = new TreeSet<Vale>();
		SortedSet<Outros> soutros = new TreeSet<Outros>();
		sitens.addAll(itens);
		svales.addAll(vales);
		soutros.addAll(outros);
		profissional.setItens(sitens);
		profissional.setVales(svales);
		profissional.setOutros(soutros);		
		return profissional;
	}	

	@Override
	public Profissional buscarProfissionalEAgendas(boolean ativo, Date data, int idProfissional) {
		StringBuilder sql = new StringBuilder("select distinct prof from Profissional prof ");
		sql.append("left outer join fetch prof.agendas agenda ");
		sql.append("left outer join fetch agenda.cliente ");
		sql.append("where prof.ativo = ").append(ativo).append(" ");
		sql.append("and prof.id = ").append(idProfissional).append(" ");
		sql.append("and agenda.data = :data ");		
		sql.append("order by agenda.id");
		Query query = em.createQuery(sql.toString());
		query.setParameter("data", data, TemporalType.DATE);	
		Profissional profissional = null;
		try{
			profissional = (Profissional) query.getSingleResult();
		}catch (EntityNotFoundException e) {
			return null;
		}catch(NoResultException e){
			return null;
		}
		return profissional;
	}

	@Override
	public Profissional buscar(String senha, String celular) {
		StringBuilder sql = new StringBuilder("from Profissional ");
		sql.append("where senha = '").append(senha).append("' ");		
		sql.append("and celular = '").append(celular).append("' ");
		sql.append("and ativo = true");
		Query query = em.createQuery(sql.toString());
		Profissional profissional = null;
		try{
			 profissional = (Profissional) query.getSingleResult();
		}catch (EntityNotFoundException e) {
			return null;
		}catch(NoResultException e){
			return null;
		}
		return profissional;
	}
}
