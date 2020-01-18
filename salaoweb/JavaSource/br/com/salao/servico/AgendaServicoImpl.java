package br.com.salao.servico;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.salao.dao.AgendaDAO;
import br.com.salao.dao.ClienteDAO;
import br.com.salao.dao.ClienteDAOImpl;
import br.com.salao.dao.ProfissionalDAO;
import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Profissional;
import br.com.salao.util.Correio;
import br.com.salao.ws.xml.modelo.Cliente;

@Service("agendaServico")
public class AgendaServicoImpl implements AgendaServico, Serializable {	
	
	private static final long serialVersionUID = 1L;	
	@Autowired
	private AgendaDAO agendaDAO;
	@Autowired
	private ProfissionalDAO profissionalDAO;
	private static Logger logg = Logger.getLogger(AgendaServicoImpl.class);
	
	public AgendaServicoImpl(){		
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public Agenda salvar(Agenda agenda) throws ServicoException {	
		try {			
			Profissional profissional = profissionalDAO.buscarProfissionalEAgendas(true, agenda.getData(), agenda.getProfissional().getId());
			if(profissional != null){
				SortedSet<Agenda> agendas = profissional.getAgendas();
				//int qtdClientesMesmoHorario = 0;
				DateFormat format = SimpleDateFormat.getTimeInstance();	
				Calendar calendar = Calendar.getInstance();				
				for(Agenda ag : agendas){		
					String agHoraDe = format.format(ag.getHoraDe());
					String agHoraAte = "";
					calendar.setTime(ag.getHoraAte());
					//calendar.add(Calendar.MINUTE, -30);
					agHoraAte = format.format(calendar.getTime());
					String agendaHoraDe = format.format(agenda.getHoraDe());
					calendar.setTime(agenda.getHoraAte());
					//calendar.add(Calendar.MINUTE, -30);					
					String agendaHoraAte = format.format(calendar.getTime());					
					if(agenda.getEncaixe() && agendaHoraDe.equals(agHoraDe) && agenda.getCliente().equals(ag.getCliente())){
						logg.error("O cliente para encaixe é mesmo da agenda selecionada: "+ag.getCliente().getNome());
						throw new ServicoException("O cliente para encaixe é mesmo da agenda selecionada: "+ag.getCliente().getNome()); 
					}//else if(agenda.getEncaixe() && agendaHoraDe.equals(agHoraDe)){
//						qtdClientesMesmoHorario++;
//						if(qtdClientesMesmoHorario == 2){
//							logg.error("Não é possivél encaixar mais um cliente.");
//							throw new ServicoException("Não é possivél encaixar mais um cliente."); 
//						}
//					}
//					else if(!agenda.getEncaixe() && (agendaHoraDe.equals(agHoraDe) || agendaHoraDe.equals(agHoraAte) || agendaHoraAte.equals(agHoraDe))){
//						logg.error("Não é possivél agendar no período selecionado. Existe 1 cliente agendado.");
//						throw new ServicoException("Não é possivél agendar no período selecionado. Existe 1 cliente agendado."); 
//					}					
				}	
			}
			return agendaDAO.salvar(agenda);			
		} catch (Exception e) {				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void atualizar(Agenda agenda) throws ServicoException {	
		try {
			Profissional profissional = profissionalDAO.buscarProfissionalEAgendas(true, agenda.getData(), agenda.getProfissional().getId());
			if(profissional != null){
				SortedSet<Agenda> agendas = profissional.getAgendas();				
				DateFormat format = SimpleDateFormat.getTimeInstance();	
				Calendar calendar = Calendar.getInstance();				
				for(Agenda ag : agendas){
					if(agenda.equals(ag)){
						continue;
					}
					String agHoraDe = format.format(ag.getHoraDe());
					String agHoraAte = "";
					calendar.setTime(ag.getHoraAte());
					//calendar.add(Calendar.MINUTE, -30);
					agHoraAte = format.format(calendar.getTime());
					String agendaHoraDe = format.format(agenda.getHoraDe());
					calendar.setTime(agenda.getHoraAte());
					//calendar.add(Calendar.MINUTE, -30);					
					String agendaHoraAte = format.format(calendar.getTime());					
//					if(agendaHoraDe.equals(agHoraDe) || agendaHoraDe.equals(agHoraAte) || agendaHoraAte.equals(agHoraDe)){
//						logg.error("Não é possivél agendar no período selecionado. Existe 1 cliente agendado.");
//						throw new ServicoException("Não é possivél agendar no período selecionado. Existe 1 cliente agendado."); 
//					}					
				}	
			}				
			agendaDAO.atualizar(agenda);
		} catch (Exception e) {			
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void excluir(Agenda agenda) throws ServicoException {
		try{			
			agenda = agendaDAO.buscar(agenda.getId());
			agendaDAO.excluir(agenda);			
		} catch (Exception e) {				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscar(Date dataInicio, Date dataFim) throws ServicoException {	
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscar(dataInicio, dataFim);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}		
		return agendas;	
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscar(Date data, boolean fechada) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscar(data, fechada);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}		
		return agendas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscarAgendaCliente(Date dataInicio, Date dataFim, Integer idCliente) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscarAgendaCliente(dataInicio, dataFim, idCliente);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException();
		}
		return agendas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscarAgendaCliente(Date data, Integer idCliente) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscarAgendaCliente(data, idCliente);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return agendas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscarAgendaProfissional(Date dataInicio, Date dataFim,	Integer idProfissional) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscarAgendaProfissional(dataInicio, dataFim, idProfissional);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return agendas;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Agenda> buscarAgendaProfissional(Date data,	Integer idProfissional) throws ServicoException {
		List<Agenda> agendas = null;
		try {
			agendas = agendaDAO.buscarAgendaProfissional(data, idProfissional);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
		return agendas;
	}

	@Transactional(rollbackFor=ServicoException.class)
	@Override
	public void fechar(List<Agenda> agendas) throws ServicoException {
		try{			
			for(Agenda ag : agendas){
				ag.setFechada(true);
				agendaDAO.atualizar(ag);
			}
		} catch (Exception e) {				
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}	
	}

	@Transactional(readOnly=true)
	@Override
	public Long buscarQunatidade(Date data) throws ServicoException {		
		try {
			return agendaDAO.buscarQunatidade(data);
		} catch (Exception e) {
			logg.error(e);
			throw new ServicoException(e.getMessage());
		}
	}	
}
