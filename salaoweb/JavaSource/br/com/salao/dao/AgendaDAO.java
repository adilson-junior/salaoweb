package br.com.salao.dao;

import java.util.Date;
import java.util.List;

import br.com.salao.modelo.Agenda;

public interface AgendaDAO {
	
	Agenda salvar(Agenda agenda);
	void atualizar(Agenda agenda);
	void excluir(Agenda agenda);
	List<Agenda> buscarAgendaProfissional(Date dataInicio, Date dataFim, Integer idProfissional);
	List<Agenda> buscarAgendaCliente(Date dataInicio, Date dataFim, Integer idCliente);
	List<Agenda> buscar(Date dataInicio, Date dataFim);
	List<Agenda> buscar(Date data, boolean fechada);
	List<Agenda> buscarAgendaProfissional(Date data, Integer idProfissional);
	List<Agenda> buscarAgendaCliente(Date data, Integer idCliente);
	Agenda buscar(int id);
	List<Agenda> buscarQunatidade(Date data, Date horaInicio, Date horaFim, Integer idProfissional);
	Long buscarQunatidade(Date data);
}
