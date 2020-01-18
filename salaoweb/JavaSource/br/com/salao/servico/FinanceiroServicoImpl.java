package br.com.salao.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.AgendaDAO;
import br.com.salao.dao.BancoDAO;
import br.com.salao.dao.BandeiraDAO;
import br.com.salao.dao.FinanceiroDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cartao;
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Pagamento;
import br.com.salao.modelo.vo.HistoricoGraficoVO;

@Service("financeiroServico")
public class FinanceiroServicoImpl implements FinanceiroServico, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@Autowired
	private FinanceiroDAO financeiroDAO;
	@Autowired
	private AgendaDAO agendaDAO;	
	@Autowired
	private BandeiraDAO bandeiraDAO;
	@Autowired
	private BancoDAO bancoDAO;
	private static Logger logg = Logger.getLogger(FinanceiroServicoImpl.class);
	
	public FinanceiroServicoImpl(){			
	}

	@Transactional(readOnly=true)
	@Override
	public List<Comanda> buscarComandasFechadas(Integer idCaixa) throws ServicoException {
		List<Comanda> comandas = null;
		try {
			comandas = financeiroDAO.buscarComandasFechadas(idCaixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
		return comandas;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvar(Comanda comanda, List<Item> itens, List<Cartao> cartoes, List<Agenda> agendas) throws ServicoException {		
		try {				
			for(Agenda agenda: agendas){
				agenda.setFechada(true);
				agendaDAO.atualizar(agenda);
			}		
			Cheque cheque = comanda.getCheque();			
			comanda.setCartoes(null);
			comanda.setCheque(null);
			comanda.setCheque(null);
			comanda = financeiroDAO.salvar(comanda);
			if(cheque != null){				
				cheque.setComanda(comanda);
				bancoDAO.salvarCheque(cheque);				
			}	
			for(Cartao cart : cartoes){
				cart.setComanda(comanda);
				bandeiraDAO.salvarCartao(cart);		
			}
			for(Item item: itens){
				item.setComanda(comanda);
				financeiroDAO.salvarItem(item);
			}			
		} catch (Exception e) {	
			logg.error("Erro ao salvar a comanda ", e);
			throw new ServicoException(e.getMessage());
		}			
	}

	@Transactional(readOnly=true)
	@Override
	public List<Comanda> buscarComandas(Date inicio, Date fim) throws ServicoException {
		List<Comanda> comandas = null;
		try {
			comandas = financeiroDAO.buscarComandas(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return comandas;
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<Comanda> buscarComandas(Date inicio, Date fim, Integer idCliente) throws ServicoException {
		List<Comanda> comandas = null;
		try {
			comandas = financeiroDAO.buscarComandas(inicio, fim, idCliente);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return comandas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Caixa> buscarCaixaAberto() throws ServicoException {
		List<Caixa> caixas = null;
		try {
			caixas = financeiroDAO.buscarCaixaAberto();
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return caixas;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarCaixa(Caixa caixa) throws ServicoException {
		try {
			financeiroDAO.salvarCaixa(caixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void atualizarCaixa(Caixa caixa) throws ServicoException {
		try {
			financeiroDAO.atualizarCaixa(caixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}		
	}

	@Transactional(readOnly=true)
	@Override
	public List<Comanda> buscarComandas(Integer idCaixa) throws ServicoException {
		List<Comanda> comandas = null;
		try {
			comandas = financeiroDAO.buscarComandas(idCaixa);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return comandas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Comanda> buscarComandasProfissional(Date inicio, Date fim, Integer idProfissional) throws ServicoException {
		List<Comanda> comandas = null;
		try {
			comandas = financeiroDAO.buscarComandasProfissional(inicio, fim, idProfissional);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return comandas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Cheque> buscarChequesPendentes() throws ServicoException {
		List<Cheque> cheques = null;
		try {
			cheques = financeiroDAO.buscarChequesPendentes();
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return cheques;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Cheque> buscarCheques(Date inicio, Date fim) throws ServicoException {
		List<Cheque> cheques = null;
		try {
			cheques = financeiroDAO.buscarCheques(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return cheques;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void resolverCheque(Cheque cheque) throws ServicoException {
		try {
			cheque = financeiroDAO.buscarCheque(cheque.getId());
			cheque.setDataPagamento(new Date());
			cheque.setPago(true);
			financeiroDAO.atualizarCheque(cheque);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoMes(Date inicio, Date fim) throws ServicoException {
		List<HistoricoGraficoVO> historicos = null;
		try {
			historicos = financeiroDAO.faturamentoMes(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return historicos;
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoMenosPagamentosMes(Date inicio, Date fim) throws ServicoException {
		List<HistoricoGraficoVO> historicos = null;
		try {
			historicos = financeiroDAO.faturamentoMenosPagamentosMes(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return historicos;
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoDia(Date inicio, Date fim) throws ServicoException {
		List<HistoricoGraficoVO> historicos = null;
		try {
			historicos = financeiroDAO.faturamentoDia(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return historicos;
	}

	@Override
	public List<HistoricoGraficoVO> faturamentoMenosComissaoDia(Date inicio, Date fim) throws ServicoException {
		List<HistoricoGraficoVO> historicos = null;
		try {
			historicos = financeiroDAO.faturamentoMenosComissaoDia(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return historicos;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Pagamento> buscarPagamentos(Date inicio, Date fim)	throws ServicoException {
		List<Pagamento> pagamentos = null;
		try {
			pagamentos = financeiroDAO.buscarPagamentos(inicio, fim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return pagamentos;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void salvarPagamentos(List<Pagamento> pagamentos) throws ServicoException {
		try {
			for(Pagamento pagamento : pagamentos){
				financeiroDAO.salvarPagamento(pagamento);
			}
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}

	@Override
	public List<Item> buscarClientesEItensFaturamentosHoje(Integer idProfissional) throws ServicoException {		
		try {
			return financeiroDAO.buscarClientesEItensFaturamentosHoje(idProfissional);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}
}
