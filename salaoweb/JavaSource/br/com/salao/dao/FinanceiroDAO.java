package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Pagamento;
import br.com.salao.modelo.vo.HistoricoGraficoVO;

public interface FinanceiroDAO {
	List<Comanda> buscarComandasFechadas(Integer idCaixa);
	List<Comanda> buscarComandas(Date inicio, Date fim);
	List<Comanda> buscarComandas(Date inicio, Date fim, Integer idCliente);
	List<Comanda> buscarComandasProfissional(Date inicio, Date fim, Integer idProfissional);
	Comanda salvar(Comanda comanda);
	List<Comanda> buscarComandas(Integer idCaixa);
	List<Caixa> buscarCaixaAberto();
	void salvarItem(Item item);
	void salvarCaixa(Caixa caixa);
	void atualizarCaixa(Caixa caixa);
	List<Cheque> buscarChequesPendentes();		
	List<Cheque> buscarCheques(Date inicio, Date fim);
	void salvarPagamento(Pagamento pagamento);
	List<Pagamento> buscarPagamentos(Date inicio, Date fim);
	Cheque buscarCheque(Integer id);
	void atualizarCheque(Cheque cheque);	
	List<HistoricoGraficoVO> faturamentoMes(Date inicio, Date fim);
	List<HistoricoGraficoVO> faturamentoMenosPagamentosMes(Date inicio, Date fim);
	List<HistoricoGraficoVO> faturamentoDia(Date inicio, Date fim);
	List<HistoricoGraficoVO> faturamentoMenosComissaoDia(Date inicio, Date fim);
	List<Item> buscarClientesEItensFaturamentosHoje(Integer idProfissional);
}
