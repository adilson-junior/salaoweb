package br.com.salao.servico;

import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Cartao;
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.Pagamento;
import br.com.salao.modelo.vo.HistoricoGraficoVO;

public interface FinanceiroServico {
	List<Comanda> buscarComandasFechadas(Integer idCaixa) throws ServicoException;
	List<Comanda> buscarComandas(Date inicio, Date fim) throws ServicoException;
	List<Comanda> buscarComandas(Date inicio, Date fim, Integer idCliente) throws ServicoException;
	List<Comanda> buscarComandasProfissional(Date inicio, Date fim, Integer idProfissional) throws ServicoException;
	List<Comanda> buscarComandas(Integer idCaixa) throws ServicoException;
	void salvar(Comanda comanda,  List<Item> itens, List<Cartao> cartoes,List<Agenda> agendas)throws ServicoException;	
	List<Caixa> buscarCaixaAberto() throws ServicoException;
	void salvarCaixa(Caixa caixa) throws ServicoException;
	void atualizarCaixa(Caixa caixa) throws ServicoException;
	List<Cheque> buscarChequesPendentes() throws ServicoException;
	List<Cheque> buscarCheques(Date inicio, Date fim) throws ServicoException;
	void resolverCheque(Cheque cheque) throws ServicoException;
	List<HistoricoGraficoVO> faturamentoMes(Date inicio, Date fim) throws ServicoException;
	List<HistoricoGraficoVO> faturamentoMenosPagamentosMes(Date inicio, Date fim) throws ServicoException;
	List<HistoricoGraficoVO> faturamentoDia(Date inicio, Date fim) throws ServicoException;
	List<HistoricoGraficoVO> faturamentoMenosComissaoDia(Date inicio, Date fim) throws ServicoException;
	List<Pagamento> buscarPagamentos(Date inicio, Date fim) throws ServicoException;
	void salvarPagamentos(List<Pagamento> pagamentos) throws ServicoException;
	List<Item> buscarClientesEItensFaturamentosHoje(Integer idProfissional)throws ServicoException;
}
