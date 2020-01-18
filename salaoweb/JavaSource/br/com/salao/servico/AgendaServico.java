package br.com.salao.servico;
import java.util.Date;
import java.util.List;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;

public interface AgendaServico {
	Agenda salvar(Agenda agenda) throws ServicoException;
	void atualizar(Agenda agenda) throws ServicoException;
	void excluir(Agenda agenda) throws ServicoException;
	void fechar(List<Agenda> agendas) throws ServicoException;
	List<Agenda> buscar(Date dataInicio, Date dataFim) throws ServicoException;
	List<Agenda> buscar(Date data, boolean fechada) throws ServicoException;	
	List<Agenda> buscarAgendaCliente(Date dataInicio, Date dataFim, Integer idCliente) throws ServicoException;
	List<Agenda> buscarAgendaCliente(Date data, Integer idCliente) throws ServicoException;	
	List<Agenda> buscarAgendaProfissional(Date dataInicio, Date dataFim, Integer idProfissional) throws ServicoException;
	List<Agenda> buscarAgendaProfissional(Date data, Integer idProfissional) throws ServicoException;	
	Long buscarQunatidade(Date data) throws ServicoException;
}
