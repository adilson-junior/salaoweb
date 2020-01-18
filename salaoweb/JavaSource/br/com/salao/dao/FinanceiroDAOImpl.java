package br.com.salao.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Repository;

import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Pagamento;
import br.com.salao.modelo.vo.HistoricoGraficoVO;
import br.com.salao.util.DateUtil;

@Repository("financeiroDAO")
public class FinanceiroDAOImpl implements FinanceiroDAO, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	private static Logger logg = Logger.getLogger(FinanceiroDAOImpl.class);
	@PersistenceContext
	private EntityManager em;
	
	public FinanceiroDAOImpl(){		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comanda> buscarComandasFechadas(Integer idCaixa) {
		StringBuilder sb = new StringBuilder("select distinct comanda from Comanda comanda ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("left outer join fetch item.profissional ");
		sb.append("left outer join fetch item.servico ");
		sb.append("left outer join fetch item.produto ");
		sb.append("left outer join fetch comanda.cartoes ");
		sb.append("left outer join fetch comanda.cheque ");
		sb.append("where comanda.caixa.id = ").append(idCaixa).append(" ");	
		sb.append("order by comanda.id desc");
		Query query = em.createQuery(sb.toString());		
		return query.getResultList();
	}

	@Override
	public Comanda salvar(Comanda comanda) {
		return em.merge(comanda);		
	}

	@Override
	public void salvarItem(Item item) {
		em.persist(item);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comanda> buscarComandas(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("select distinct comanda from Comanda comanda ");		
		sb.append("left outer join fetch comanda.cartoes ");
		sb.append("left outer join fetch comanda.cheque ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("where comanda.data between :inicio and :fim ");	
		sb.append("order by comanda.data");
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
	public List<Comanda> buscarComandas(Date inicio, Date fim, Integer idCliente) {
		StringBuilder sb = new StringBuilder("select distinct comanda from Comanda comanda ");		
		sb.append("left outer join fetch comanda.cartoes ");
		sb.append("left outer join fetch comanda.cheque ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("where comanda.data between :inicio and :fim ");	
		sb.append("and item.cliente.id = ").append(idCliente).append(" ");
		sb.append("order by comanda.data");
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
	public List<Comanda> buscarComandasProfissional(Date inicio, Date fim, Integer idProfissional) {
		StringBuilder sb = new StringBuilder("select distinct comanda from Comanda comanda ");			
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");	
		sb.append("where comanda.data between :inicio and :fim ");	
		sb.append("and item.profissional.id = ").append(idProfissional).append(" ");
		sb.append("order by comanda.data");
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
	public List<Comanda> buscarComandas(Integer idCaixa) {
		StringBuilder sb = new StringBuilder("select distinct comanda from Comanda comanda ");		
		sb.append("left outer join fetch comanda.cartoes ");
		sb.append("left outer join fetch comanda.cheque ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("where comanda.caixa.id = ").append(idCaixa);		
		Query query = em.createQuery(sb.toString());
		return query.getResultList();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Caixa> buscarCaixaAberto() {
		StringBuilder sb = new StringBuilder("from Caixa caixa ");	
		sb.append("left outer join fetch caixa.reforcos ");
		sb.append("left outer join fetch caixa.sangrias ");			
		sb.append("where caixa.status = 1");		
		Query query = em.createQuery(sb.toString());		
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cheque> buscarChequesPendentes(){
		StringBuilder sb = new StringBuilder("select distinct cheque from Cheque cheque ");		
		sb.append("left outer join fetch cheque.comanda comanda ");	
		sb.append("left outer join fetch cheque.banco ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("where cheque.pago = false ");
		sb.append("order by cheque.data");
		Query query = em.createQuery(sb.toString());
		return query.getResultList();	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cheque> buscarCheques(Date inicio, Date fim){
		StringBuilder sb = new StringBuilder("select distinct cheque from Cheque cheque ");		
		sb.append("left outer join fetch cheque.comanda comanda ");	
		sb.append("left outer join fetch cheque.banco ");
		sb.append("left outer join fetch comanda.itens item ");
		sb.append("left outer join fetch item.cliente ");
		sb.append("where cheque.data between :inicio and :fim ");	
		sb.append("and cheque.pago = true ");
		sb.append("order by cheque.data");
		Query query = em.createQuery(sb.toString());
		DateUtil dateUtil = new DateUtil();
		inicio = dateUtil.getDataHora(inicio, 0, 0, 0);
		fim = dateUtil.getDataHora(fim, 23, 59, 59);
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);
		return query.getResultList();	
	}	

	@Override
	public void salvarCaixa(Caixa caixa) {
		em.persist(caixa);		
	}

	@Override
	public void atualizarCaixa(Caixa caixa) {
		em.merge(caixa);		
	}

	@Override
	public Cheque buscarCheque(Integer id) {
		StringBuilder sb = new StringBuilder("from Cheque cheque ");		
		sb.append("where cheque.id = ").append(id);
		Query query = em.createQuery(sb.toString());
		return (Cheque) query.getSingleResult();
	}

	@Override
	public void atualizarCheque(Cheque cheque) {
		em.merge(cheque);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pagamento> buscarPagamentos(Date inicio, Date fim) {
		StringBuilder sb = new StringBuilder("from Pagamento pag ");		
		sb.append("left outer join fetch pag.profissional prof ");			
		sb.append("where pag.de between :inicio and :fim ");	
		sb.append("order by prof.nome ");
		Query query = em.createQuery(sb.toString());		
		query.setParameter("inicio", inicio, TemporalType.DATE);
		query.setParameter("fim", fim, TemporalType.DATE);
		return query.getResultList();			
	}
	
	@Override
	public void salvarPagamento(Pagamento pagamento) {		
		em.persist(pagamento);
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Item> buscarClientesEItensFaturamentosHoje(Integer idProfissional) {
		Date hoje = new Date();		
		DateUtil dateUtil = new DateUtil();
		Date inicio = dateUtil.getDataHora(hoje, 0, 0, 0);
		Date fim = dateUtil.getDataHora(hoje, 23, 59, 59);	
		StringBuilder sql = new StringBuilder("select distinct item from Item item ");	
		sql.append("left outer join fetch item.cliente ");
		sql.append("where item.data between :inicio and :fim ");
		sql.append("and item.profissional.id = ").append(idProfissional).append(" ");
		sql.append("order by item.data desc");
		Query query = em.createQuery(sql.toString());		
		query.setParameter("inicio", inicio, TemporalType.TIMESTAMP);
		query.setParameter("fim", fim, TemporalType.TIMESTAMP);		
		return query.getResultList();		
	}
	
	@Override
	public List<HistoricoGraficoVO> faturamentoMes(Date inicio, Date fim){
		Connection  conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;	
		List<HistoricoGraficoVO> historicos = new ArrayList<HistoricoGraficoVO>();
		try{
			DateUtil dateUtil = new DateUtil();
			conn = getConnection();			
			StringBuilder query = new StringBuilder("select (sum(valor * quantidade)- sum(desconto)) as valor, date_part('month',data) as mes ");	
			query.append("from item ");
			query.append("where data between '")
			.append(dateUtil.formatarParaDataSQL(inicio))
			.append(" 00:00:00' and '")
			.append(dateUtil.formatarParaDataSQL(fim))
			.append(" 23:59:59' ");
			query.append("group by date_part('year',data), date_part('month',data) ");
			query.append("order by date_part('year',data), date_part('month',data)");
			stm = conn.prepareStatement(query.toString());
			rs = stm.executeQuery();			
			while(rs.next()){
				HistoricoGraficoVO historico = new HistoricoGraficoVO();
				historico.setNome(dateUtil.getNomeMes(rs.getInt("mes")));
				historico.setValor(rs.getFloat("valor"));
				historicos.add(historico);
			}			
		} catch (SQLException e) {
			logg.error("Error ao abrir uma conexão com o Cache", e);			
		} finally{
			if(stm != null){
				try {
					stm.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o PreparedStatement", e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o ResultSet", e);
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar a conexão Cache", e);
				}
			}			
		}
		return historicos;
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoMenosPagamentosMes(Date inicio, Date fim) {
		Connection  conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;	
		List<HistoricoGraficoVO> historicos = new ArrayList<HistoricoGraficoVO>();
		try{
			DateUtil dateUtil = new DateUtil();
			conn = getConnection();			
			StringBuilder query = new StringBuilder("select (sum(valor * quantidade) - sum(desconto)) as valor, ");	
			query.append("sum(((valor * quantidade) - desconto) * (comissao / 100)) as comissao, date_part('month',data) as mes ");
			query.append("from item ");
			query.append("where data between '")
			.append(dateUtil.formatarParaDataSQL(inicio))
			.append(" 00:00:00' and '")
			.append(dateUtil.formatarParaDataSQL(fim))
			.append(" 23:59:59' ");
			query.append("group by date_part('year',data), date_part('month',data) ");
			query.append("order by date_part('year',data), date_part('month',data)");
			stm = conn.prepareStatement(query.toString());
			rs = stm.executeQuery();			
			while(rs.next()){
				HistoricoGraficoVO historico = new HistoricoGraficoVO();
				historico.setNome(dateUtil.getNomeMes(rs.getInt("mes")));				
				historico.setValor(rs.getFloat("valor"));
				historicos.add(historico);
			}	
			stm.close();
			rs.close();
			query = new StringBuilder("select sum(valor) + sum(vale + outros + ajuste) as total, date_part('month',de) as mes ");	
			query.append("from pagamento ");		
			query.append("where de between '")
			.append(dateUtil.formatarParaDataSQL(inicio))
			.append(" 00:00:00' and '")
			.append(dateUtil.formatarParaDataSQL(fim))
			.append(" 23:59:59' ");
			query.append("group by date_part('year',de), date_part('month',de) ");
			query.append("order by date_part('year',de), date_part('month',de)");
			stm = conn.prepareStatement(query.toString());
			rs = stm.executeQuery();			
			while(rs.next()){
				String mes = dateUtil.getNomeMes(rs.getInt("mes"));
				for(HistoricoGraficoVO hist : historicos){
					if(hist.getNome().equals(mes)){
						hist.setValor(hist.getValor() - rs.getFloat("total"));
						break;
					}
				}
			}				
		} catch (SQLException e) {
			logg.error("Error ao abrir uma conexão com o Cache", e);			
		} finally{
			if(stm != null){
				try {
					stm.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o PreparedStatement", e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o ResultSet", e);
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar a conexão Cache", e);
				}
			}			
		}
		return historicos;
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoDia(Date inicio, Date fim) {
		Connection  conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;	
		List<HistoricoGraficoVO> historicos = new ArrayList<HistoricoGraficoVO>();
		try{
			DateUtil dateUtil = new DateUtil();
			conn = getConnection();			
			StringBuilder query = new StringBuilder("select (sum(valor * quantidade) - sum(desconto)) as valor, ");	
			query.append("date_part('month',data) as mes, date_part('day',data) as dia ");
			query.append("from item ");
			query.append("where data between '")
			.append(dateUtil.formatarParaDataSQL(inicio))
			.append(" 00:00:00' and '")
			.append(dateUtil.formatarParaDataSQL(fim))
			.append(" 23:59:59' ");
			query.append("group by date_part('month',data), date_part('day',data) ");
			query.append("order by date_part('month',data), date_part('day',data)");
			stm = conn.prepareStatement(query.toString());
			rs = stm.executeQuery();			
			while(rs.next()){
				HistoricoGraficoVO historico = new HistoricoGraficoVO();
				int d = rs.getInt("dia");
				String dia = d < 10 ? "0"+d : ""+d;
				historico.setNome(dia);				
				historico.setValor(rs.getFloat("valor"));
				historicos.add(historico);
			}			
		} catch (SQLException e) {
			logg.error("Error ao abrir uma conexão com o Cache", e);			
		} finally{
			if(stm != null){
				try {
					stm.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o PreparedStatement", e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o ResultSet", e);
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar a conexão Cache", e);
				}
			}			
		}
		return historicos;
	}
	
	@Override
	public List<HistoricoGraficoVO> faturamentoMenosComissaoDia(Date inicio, Date fim) {
		Connection  conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;	
		List<HistoricoGraficoVO> historicos = new ArrayList<HistoricoGraficoVO>();
		try{
			DateUtil dateUtil = new DateUtil();
			conn = getConnection();			
			StringBuilder query = new StringBuilder("select (sum(valor * quantidade) - sum(desconto)) as valor, ");	
			query.append("date_part('month',data) as mes, date_part('day',data) as dia ");
			query.append("from item ");
			query.append("where data between '")
			.append(dateUtil.formatarParaDataSQL(inicio))
			.append(" 00:00:00' and '")
			.append(dateUtil.formatarParaDataSQL(fim))
			.append(" 23:59:59' ");
			query.append("group by date_part('month',data), date_part('day',data) ");
			query.append("order by date_part('month',data), date_part('day',data)");
			stm = conn.prepareStatement(query.toString());
			rs = stm.executeQuery();			
			while(rs.next()){
				HistoricoGraficoVO historico = new HistoricoGraficoVO();
				int d = rs.getInt("dia");
				String dia = d < 10 ? "0"+d : ""+d;
				historico.setNome(dia);				
				historico.setValor(rs.getFloat("valor"));
				historicos.add(historico);
			}			
		} catch (SQLException e) {
			logg.error("Error ao abrir uma conexão com o Cache", e);			
		} finally{
			if(stm != null){
				try {
					stm.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o PreparedStatement", e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar o ResultSet", e);
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					logg.error("Error ao fechar a conexão Cache", e);
				}
			}			
		}
		return historicos;
	}
	
	private Connection getConnection() throws SQLException{
		Session s = (Session) em.getDelegate();		
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) s.getSessionFactory();
		ConnectionProvider cp = sfi.getConnectionProvider();		
		return cp.getConnection();
	}	
}
