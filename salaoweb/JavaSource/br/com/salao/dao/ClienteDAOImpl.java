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

import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;

@Repository("clienteDAO")
public class ClienteDAOImpl implements ClienteDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@PersistenceContext
	private EntityManager em;
	
	public ClienteDAOImpl(){		
	}

	@Override
	public Cliente buscar(Integer id) {
		if(id == 0){
			return null;
		}	
		Cliente cliente = null;
		try{
			cliente = em.find(Cliente.class, id);
		}catch(EntityNotFoundException e){
			return null;
		}catch(NoResultException e){
			return null;
		}
		return cliente;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> listar(boolean ativo) {
		StringBuilder sql = new StringBuilder("from Cliente ");
		sql.append("  where ativo = ").append(ativo).append(" ");
		sql.append("order by nome");
		Query query = em.createQuery(sql.toString());
		List<Cliente> clientes = query.getResultList();				
		return clientes;
	}

	@Override
	public void salvar(Cliente cliente) {
		em.persist(cliente);
	}

	@Override
	public void alterar(Cliente cliente) {
		em.merge(cliente);
	}

	@Override
	public void exlcuir(Cliente cliente) {
		em.remove(cliente);
	}

	@Override
	public Long buscarQuantidadeAgendas(Integer idCliente) {
		StringBuilder sql = new StringBuilder("select count(ag) from Agenda ag ");
		sql.append("where ag.cliente.id = ").append(idCliente);
		Query query = em.createQuery(sql.toString());
		return (Long) query.getSingleResult();
	}

	@Override
	public Long buscarAgendas(Integer idCliente, Date inicio, Date fim) {
		
		return null;
	}

	@Override
	public Long buscarQuantidadeAgendas(Integer idCliente, Date inicio) {
		StringBuilder sql = new StringBuilder("select count(ag) from Agenda ag ");
		sql.append("where ag.cliente.id = ").append(idCliente).append(" ");
		sql.append("and ag.data >= :inicio");
		Query query = em.createQuery(sql.toString());
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);		
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> buscar(String palavraChave) {
		StringBuilder sql = new StringBuilder("from Cliente ");
		sql.append("where LOWER(nome) like '%").append(palavraChave.toLowerCase()).append("%' ");
		sql.append("or telefone like '%").append(palavraChave).append("%' ");
		sql.append("or celular like '%").append(palavraChave).append("%' ");
		sql.append("and ativo = true order by nome");
		Query query = em.createQuery(sql.toString());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agenda> buscarAgendas(Integer idCliente) {
		StringBuilder sql = new StringBuilder("from Agenda agenda ");		
		sql.append("left outer join fetch agenda.servicos ");	
		sql.append("left outer join fetch agenda.profissional ");
		sql.append("where agenda.fechada = false ");	
		sql.append("and agenda.cliente.id  = ").append(idCliente).append(" "); 
		sql.append("order by agenda.id");
		Query query = em.createQuery(sql.toString());			
		return query.getResultList();			
	}

	@Override
	public Cliente buscarClienteEAgendas(Date data, Integer id) {
		StringBuilder sql = new StringBuilder("select distinct cliente from Cliente cliente ");		
		sql.append("left outer join fetch cliente.agendas agenda ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("left outer join fetch agenda.profissional ");	
		sql.append("where agenda.fechada = false ");	
		sql.append("and cliente.id  = ").append(id).append(" "); 
		sql.append("and agenda.data = :data ");
		sql.append("order by agenda.id");
		Query query = em.createQuery(sql.toString());		
		query.setParameter("data",data, TemporalType.DATE);		
		Cliente cliente = null;
		try{
			cliente = (Cliente) query.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}catch (EntityNotFoundException e) {
			return null;
		}		
		return cliente;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> listar(int idCliente, int idClienteEncaixe) {
		StringBuilder sql = new StringBuilder("from Cliente cliente ");			
		sql.append("where cliente.id in(").append(idCliente);	
		sql.append(",").append(idClienteEncaixe).append(")"); 		
		Query query = em.createQuery(sql.toString());			
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> buscarClientesEAgendas(Date data) {
		StringBuilder sql = new StringBuilder("select distinct cliente from Cliente cliente ");		
		sql.append("left outer join fetch cliente.agendas agenda ");
		sql.append("left outer join fetch agenda.servicos ");
		sql.append("left outer join fetch agenda.profissional profissional ");		
		sql.append("where agenda.fechada = false ");		
		sql.append("and agenda.data = :data ");
		sql.append("order by agenda.id");
		Query query = em.createQuery(sql.toString());		
		query.setParameter("data",data, TemporalType.DATE);
		List<Cliente> clientes = query.getResultList();		
		return clientes;
	}

}
