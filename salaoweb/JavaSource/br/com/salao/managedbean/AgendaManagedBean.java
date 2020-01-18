package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Servico;
import br.com.salao.servico.AgendaServico;
import br.com.salao.servico.ClienteServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.ServicoServico;
import br.com.salao.util.Correio;
import br.com.salao.util.DateUtil;

@ManagedBean(name="agendaMB")
@ViewScoped
public class AgendaManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	private ScheduleModel eventModel;
	private ScheduleEvent event;
	private String titulo;
	private List<Profissional> profissionais;
	private List<Cliente> clientes;	
	private Map<String, Agenda> mapAgendas;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(name="clienteServico",value="#{clienteServico}")
	private ClienteServico clienteServico;
	@ManagedProperty(name="servicoServico",value="#{servicoServico}")
	private ServicoServico servicoServico;
	@ManagedProperty(name="agendaServico",value="#{agendaServico}")
	private AgendaServico agendaServico;
	private Profissional profissional;
	private Cliente cliente;
	private Cliente clienteEditar;
	private Profissional profissionalEditar;
	private DualListModel<Servico> dualServicos;
	private List<Servico> fonteServicos;	
	private Date dataSelecionada;
	private Date dataHoraIni;
	private Date dataHoraFim;
	private Date dataScheduleIni;
	private Date dataScheduleFim;
	private boolean encaixarCliente;
	private boolean editarAgenda;
	private boolean clienteSelecionado;
	private boolean editarBlockCliente;
	private Correio correio;
	/*
	 * Se true a agenda não será listada na comanda.
	 */
	private boolean fecharAgenda;
	
	public AgendaManagedBean(){
		event = new DefaultScheduleEvent();
		dataSelecionada = new Date();
		clienteEditar = new Cliente();
		profissionalEditar = new Profissional();
		cliente = new Cliente();
		profissional = new Profissional();
		profissionais = new ArrayList<Profissional>();
		clientes = new ArrayList<Cliente>();		
		titulo = "Agenda";		
		fonteServicos = new ArrayList<Servico>();	
		setDualServicos(new DualListModel<Servico>(new ArrayList<Servico>(), new ArrayList<Servico>()));
		dataHoraIni = new Date();
		dataHoraFim = new Date();
		mapAgendas = new HashMap<String, Agenda>();
		encaixarCliente = false;
		editarAgenda = true;		
		dataScheduleIni = new Date();
		dataScheduleFim = new Date();
		correio = new Correio();
	}
	
	@PostConstruct
	public void iniciar(){
		listarProfissionais();
		listarClientes();
		listarServicos();	
		eventModel = new LazyScheduleModel(){			
			private static final long serialVersionUID = 1L;
			@Override
            public void loadEvents(Date start, Date end) { 		
				dataScheduleIni.setTime(start.getTime());
				dataScheduleFim.setTime(end.getTime());
				if(profissional.getId() != null){			
					carregarAgendaProfissional();
				}else if(cliente.getId() != null){
					carregarAgendaCliente();
				}else{
					carregarAgendas();
				}
            } 
		};
	}
	
	public void addEvent(ActionEvent actionEvent) throws Exception{
		if(cliente == null || cliente.getId() == null
				|| profissional == null|| profissional.getId() == null 
				|| dualServicos.getTarget().size() == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Agenda", "Para concluir selecione um cliente, um profissional e no mínimo um serviço.");
			return;
		}		
		if(clienteSelecionado){
			((DefaultScheduleEvent)event).setTitle(profissional.getNome());
		}else{
			((DefaultScheduleEvent)event).setTitle(cliente.getNome());
		}
		// Calendar para corrigir o problema do timezone primefaces.
		// A data não seta como atual quando seta o ScheduleEvent (inicio e fim)
		Calendar calEvent = Calendar.getInstance();
		calEvent.setTime(dataHoraIni);		
		Calendar calendarIni = Calendar.getInstance();		
		calendarIni.setTime(dataSelecionada);
		calendarIni.set(Calendar.HOUR_OF_DAY, calEvent.get(Calendar.HOUR_OF_DAY));		
		//calendarIni.set(Calendar.MINUTE, 0);	
		calendarIni.set(Calendar.MINUTE, calEvent.get(Calendar.MINUTE));
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(dataSelecionada);			
		calEvent.setTime(dataHoraFim);
		calendarFim.set(Calendar.HOUR_OF_DAY, calEvent.get(Calendar.HOUR_OF_DAY));		
		//calendarFim.set(Calendar.MINUTE, 0);	
		calendarFim.set(Calendar.MINUTE, calEvent.get(Calendar.MINUTE));
		Agenda agenda = new Agenda();	
		agenda.setCliente(cliente);		
		agenda.setProfissional(profissional);
		agenda.setHoraDe(calendarIni.getTime());
		agenda.setHoraAte(calendarFim.getTime());
		agenda.getServicos().addAll(dualServicos.getTarget());
		agenda.setData(dataSelecionada);
		agenda.setDataCriacao(new Date());
		//Ajustando a data para exibir o evento na agenda
		// Não uso os minitos (inicio e fim) para agendar			
		calendarFim.set(Calendar.HOUR_OF_DAY, calendarFim.get(Calendar.HOUR_OF_DAY)+1);
		((DefaultScheduleEvent)event).setStartDate(calendarIni.getTime());				
		((DefaultScheduleEvent)event).setEndDate(calendarFim.getTime());
		try {
			agenda.setEncaixe(encaixarCliente);
			// Se true a agenda não será listada em comandas abertas
			agenda.setFechada(fecharAgenda);
			agenda = agendaServico.salvar(agenda);			
			//Teste email
			if(!cliente.getEmail().equals("")) {
			   correio.enviar(agenda, cliente, profissional);
			}
			addMensagem(FacesMessage.SEVERITY_INFO, "Agenda", "Agenda criada com sucesso");		
			setStatusEventAgenda(agenda, event);
			eventModel.addEvent(event);	
			mapAgendas.put(event.getId(), agenda);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Agenda", e.getMessage());
		}	
		finally{
			correio = new Correio();
			fecharAgenda = false;
			encaixarCliente = false;
			dataHoraIni = new Date();
			dataHoraFim = new Date();		
			cliente = new Cliente();			
			dataSelecionada = new Date();		
			event = new DefaultScheduleEvent();
			dualServicos.getSource().clear();
			dualServicos.getSource().addAll(fonteServicos);
			dualServicos.getTarget().clear();
		}		
	}
	
	public void atualizarEvent(ActionEvent actionEvent){
		if(clienteEditar == null || clienteEditar.getId() == null
				|| profissionalEditar == null|| profissionalEditar.getId() == null 
				|| dualServicos.getTarget().size() == 0){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Agenda", "Para concluir selecione um cliente, um profissional e no mínimo um serviço.");
			return;
		}		
		if(clienteSelecionado){
			((DefaultScheduleEvent)event).setTitle(profissionalEditar.getNome());
		}else{
			((DefaultScheduleEvent)event).setTitle(clienteEditar.getNome());
		}
		// Calendar para corrigir o problema do timezone primefaces.
		// A data não seta como atual quando seta o ScheduleEvent (inicio e fim)
		Calendar calEvent = Calendar.getInstance();
		calEvent.setTime(dataHoraIni);		
		Calendar calendarIni = Calendar.getInstance();		
		calendarIni.setTime(dataSelecionada);
		calendarIni.set(Calendar.HOUR_OF_DAY, calEvent.get(Calendar.HOUR_OF_DAY));			
		calendarIni.set(Calendar.MINUTE, calEvent.get(Calendar.MINUTE));	
		Calendar calendarFim = Calendar.getInstance();	
		calendarFim.setTime(dataSelecionada);			
		calEvent.setTime(dataHoraFim);
		calendarFim.set(Calendar.HOUR_OF_DAY, calEvent.get(Calendar.HOUR_OF_DAY));		
		calendarFim.set(Calendar.MINUTE, calEvent.get(Calendar.MINUTE));	
		Agenda agenda = mapAgendas.get(event.getId());
		agenda.setCliente(clienteEditar);		
		agenda.setProfissional(profissionalEditar);
		agenda.setHoraDe(calendarIni.getTime());
		agenda.setHoraAte(calendarFim.getTime());
		agenda.getServicos().clear();
		agenda.getServicos().addAll(dualServicos.getTarget());
		agenda.setData(dataSelecionada);
		agenda.setDataCriacao(new Date());
		//Ajustando a data para exibir o evento na agenda
		// Não uso os minitos (inicio e fim) para agendar			
		calendarFim.set(Calendar.HOUR_OF_DAY, calendarFim.get(Calendar.HOUR_OF_DAY)+1);
		((DefaultScheduleEvent)event).setStartDate(calendarIni.getTime());				
		((DefaultScheduleEvent)event).setEndDate(calendarFim.getTime());
		try {
			agendaServico.atualizar(agenda);			
			addMensagem(FacesMessage.SEVERITY_INFO, "Agenda", "Agenda atualizada com sucesso");		
			setStatusEventAgenda(agenda, event);
			eventModel.updateEvent(event);			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Agenda", e.getMessage());
		}	
		finally{
			dataHoraIni = new Date();
			dataHoraFim = new Date();					
			clienteEditar = new Cliente();			
			dataSelecionada = new Date();		
			event = new DefaultScheduleEvent();
			dualServicos.getSource().clear();
			dualServicos.getSource().addAll(fonteServicos);
			dualServicos.getTarget().clear();
		}		
	}
	
	public void excluir(){		
		try {
			Agenda agenda = mapAgendas.get(event.getId());
			agendaServico.excluir(agenda);
			eventModel.deleteEvent(event);
			addMensagem(FacesMessage.SEVERITY_INFO, "Agenda", "Agenda excluída com sucesso");		
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Agenda", e.getMessage());
		}
	}
	
	public void onDateSelect(SelectEvent selectEvent) {	
		encaixarCliente = false;
		editarBlockCliente = false;
		dualServicos.getSource().clear();
    	dualServicos.getSource().addAll(fonteServicos);	
    	DateUtil dateUtil = new DateUtil();
		dataSelecionada = (Date) selectEvent.getObject();	
		Calendar hoje = Calendar.getInstance();
		hoje.setTime(dateUtil.getDataHora(hoje.getTime(), 0, 0, 0));
    	Calendar dtSelecionada = Calendar.getInstance();
    	dtSelecionada.setTime(dateUtil.getDataHora(dataSelecionada, 0, 0, 0));    	 
//    	if(dtSelecionada.getTime().before(hoje.getTime()) || clienteSelecionado){    	
//    		editarAgenda = false;
//    	}else{
    		editarAgenda = true;
// 		}    	
		if(clienteSelecionado){
			editarAgenda = false;
	    }    		
		dualServicos.getTarget().clear();
		dataHoraIni = (Date) selectEvent.getObject();
		dataHoraFim = (Date) selectEvent.getObject();
        event = new DefaultScheduleEvent("", dataHoraIni, dataHoraFim);       
    }
	
	public void onEventSelect(SelectEvent selectEvent) {	
		 encaixarCliente = false;
		 editarBlockCliente = true;
	     event = (ScheduleEvent) selectEvent.getObject();	   
	     Agenda ag = mapAgendas.get(event.getId());
    	 clienteEditar = ag.getCliente();
    	 profissionalEditar = ag.getProfissional();
    	 DateUtil dateUtil = new DateUtil();
    	 dataSelecionada = ag.getData();
    	 Calendar hoje = Calendar.getInstance();
    	 hoje.setTime(dateUtil.getDataHora(hoje.getTime(), 0, 0, 0));
    	 Calendar dtSelecionada = Calendar.getInstance();
    	 dtSelecionada.setTime(dateUtil.getDataHora(dataSelecionada, 0, 0, 0));    	 
//    	 if(dtSelecionada.getTime().before(hoje.getTime()) || clienteSelecionado){
//    		 editarAgenda = false;
//    	 }else{
 			editarAgenda = true;
// 		 }
 		 if(clienteSelecionado){
    		editarAgenda = false;
    	 }
    	 dataHoraIni = ag.getHoraDe();
    	 dataHoraFim = ag.getHoraAte();    
    	 dualServicos.getSource().clear();
     	 dualServicos.getSource().addAll(fonteServicos);	
    	 dualServicos.getTarget().clear();
    	 dualServicos.getTarget().addAll(ag.getServicos());
    	 dualServicos.getSource().removeAll(ag.getServicos());    	 
	}
	 
	public void listarProfissionais(){
		try {
			profissionais = null;
			profissionais = profissionalServico.listarProfissionalPagamentoComissao();		
			if(profissionais.size() > 0){
				profissional = profissionais.get(0);
				titulo = "Agenda - Profissional "+profissional.getNome();
			}
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	 
	public List<Profissional> pesquisarProfissional(String chave){ 
		List<Profissional> profis = new ArrayList<Profissional>();
		for(Profissional prof : profissionais){
			if(prof.getNome().toLowerCase().startsWith(chave)){
				profis.add(prof);
			}
		}
		return profis;
	}
	
	private void listarClientes(){
		try {
			clientes = null;
			clientes = clienteServico.listar(true);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public List<Cliente> pesquisarCliente(String chave){ 
		chave = chave.trim();
		List<Cliente> clis = new ArrayList<Cliente>();
		for(Cliente cli : clientes){
			if(cli.getNome().toLowerCase().startsWith(chave)){
				clis.add(cli);
			}
		}
		return clis;
	}
	
	private void listarServicos(){
		try {
			fonteServicos.clear();
			fonteServicos.addAll(servicoServico.listar(true));	
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void selecionarProfissional(){
		clienteSelecionado = false;
		titulo = "Agenda - Profissional "+profissional.getNome();
		// desmacar o cliente no select
		cliente = new Cliente();
		carregarAgendaProfissional();
	}
	
	public void selecionarCliente(){
		clienteSelecionado = true;
		for(Cliente cli : clientes){
			if(cli.equals(cliente)){
				titulo = "Agenda - Cliente "+cli.getNome()+" - "+cli.getCelular();
				break;
			}
		}		
		// desmarcar o profissional no select
		profissional = new Profissional();
		carregarAgendaCliente();
	}
	
	private void carregarAgendaProfissional(){		
		List<Agenda> agendas = null;
		try {
			if((dataScheduleFim.getTime() - dataScheduleIni.getTime()) == 86400000){					
				agendas = agendaServico.buscarAgendaProfissional(dataScheduleIni, profissional.getId());
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataScheduleFim);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				agendas = agendaServico.buscarAgendaProfissional(dataScheduleIni, calendar.getTime(), profissional.getId());
			}
			carregarEventos(agendas);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void carregarAgendaCliente(){		
		List<Agenda> agendas = null;
		try {
			if((dataScheduleFim.getTime() - dataScheduleIni.getTime()) == 86400000){					
				agendas = agendaServico.buscarAgendaCliente(dataScheduleIni, cliente.getId());
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataScheduleFim);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				agendas = agendaServico.buscarAgendaCliente(dataScheduleIni, calendar.getTime(), cliente.getId());
			}			
			carregarEventos(agendas);
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	private void carregarAgendas(){
		try {	
			List<Agenda> agendas = null;
			if((dataScheduleFim.getTime() - dataScheduleIni.getTime()) == 86400000){					
				agendas = agendaServico.buscar(dataScheduleIni, dataScheduleIni);
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataScheduleFim);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				agendas = agendaServico.buscar(dataScheduleIni, calendar.getTime());
			}		
			carregarEventos(agendas);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}	
	
	private void carregarEventos(List<Agenda> agendas){
		mapAgendas.clear();
		eventModel.clear();
		for(Agenda ag : agendas){
			ScheduleEvent event = new DefaultScheduleEvent();				
			if(clienteSelecionado){
				((DefaultScheduleEvent)event).setTitle(ag.getProfissional().getNome());
			}else{
				((DefaultScheduleEvent)event).setTitle(ag.getCliente().getNome());
			}		
			Calendar calEvent = Calendar.getInstance();
			calEvent.setTime(ag.getData());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ag.getHoraDe());
			int horaDe = calendar.get(Calendar.HOUR_OF_DAY);
			int minDe = calendar.get(Calendar.MINUTE);
			calEvent.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			calEvent.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			((DefaultScheduleEvent)event).setStartDate(calEvent.getTime());
			calendar = Calendar.getInstance();
			calendar.setTime(ag.getHoraAte());			
			if(calendar.get(Calendar.MINUTE) > 0 && calendar.get(Calendar.HOUR_OF_DAY) > horaDe && minDe == 30){
				calEvent.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
				calEvent.add(Calendar.MINUTE, 0);
			}else if(calendar.get(Calendar.MINUTE) > 0){
				calEvent.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
				calEvent.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE));				
			}else{
				calEvent.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
				calEvent.set(Calendar.MINUTE, 0);
			}
			((DefaultScheduleEvent)event).setEndDate(calEvent.getTime());		
			setStatusEventAgenda(ag, event);
			eventModel.addEvent(event);
			mapAgendas.put(event.getId(), ag);
		}
	}
	
	private void setStatusEventAgenda(Agenda ag, ScheduleEvent event){		
		Calendar calEvent = Calendar.getInstance();
		calEvent.setTime(ag.getData());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ag.getHoraDe());
		Calendar hoje = Calendar.getInstance();
		if(ag.getEncaixe() 
				&& calEvent.get(Calendar.MONTH) == hoje.get(Calendar.MONTH) 
				&& calEvent.get(Calendar.DAY_OF_MONTH) >= hoje.get(Calendar.DAY_OF_MONTH)
				&& calendar.get(Calendar.HOUR_OF_DAY) >= hoje.get(Calendar.HOUR_OF_DAY)){
			((DefaultScheduleEvent)event).setStyleClass("event-agenda-encaixe");
		}if(calEvent.get(Calendar.MONTH) < hoje.get(Calendar.MONTH) 
				|| (calEvent.get(Calendar.MONTH) == hoje.get(Calendar.MONTH) 
				&& calEvent.get(Calendar.DAY_OF_MONTH) < hoje.get(Calendar.DAY_OF_MONTH))){
			((DefaultScheduleEvent)event).setStyleClass("event-agenda-dia-passado");
		}else if(calEvent.get(Calendar.MONTH) == hoje.get(Calendar.MONTH) 
				&& calEvent.get(Calendar.DAY_OF_MONTH) == hoje.get(Calendar.DAY_OF_MONTH) 
				&&  calendar.get(Calendar.HOUR_OF_DAY) < hoje.get(Calendar.HOUR_OF_DAY)){
			((DefaultScheduleEvent)event).setStyleClass("event-agenda-dia-hora-passado");
		}	
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public ClienteServico getClienteServico() {
		return clienteServico;
	}

	public void setClienteServico(ClienteServico clienteServico) {
		this.clienteServico = clienteServico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public DualListModel<Servico> getDualServicos() {
		return dualServicos;
	}

	public void setDualServicos(DualListModel<Servico> dualServicos) {
		this.dualServicos = dualServicos;
	}

	public Date getDataSelecionada() {
		return dataSelecionada;
	}

	public void setDataSelecionada(Date dataSelecionada) {
		this.dataSelecionada = dataSelecionada;
	}

	public ServicoServico getServicoServico() {
		return servicoServico;
	}

	public void setServicoServico(ServicoServico servicoServico) {
		this.servicoServico = servicoServico;
	}

	public void setAgendaServico(AgendaServico agendaServico) {
		this.agendaServico = agendaServico;
	}

	public Date getDataHoraIni() {
		return dataHoraIni;
	}

	public void setDataHoraIni(Date dataHoraIni) {
		this.dataHoraIni = dataHoraIni;
	}

	public Date getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(Date dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public Cliente getClienteEditar() {
		return clienteEditar;
	}

	public void setClienteEditar(Cliente clienteEditar) {
		this.clienteEditar = clienteEditar;
	}

	public Profissional getProfissionalEditar() {
		return profissionalEditar;
	}

	public void setProfissionalEditar(Profissional profissionalEditar) {
		this.profissionalEditar = profissionalEditar;
	}

	public boolean isEncaixarCliente() {
		return encaixarCliente;
	}

	public void setEncaixarCliente(boolean encaixarCliente) {
		this.encaixarCliente = encaixarCliente;
	}

	public boolean isEditarAgenda() {
		return editarAgenda;
	}

	public void setEditarAgenda(boolean editarAgenda) {
		this.editarAgenda = editarAgenda;
	}

	public boolean isEditarBlockCliente() {
		return editarBlockCliente;
	}

	public void setEditarBlockCliente(boolean editarBlockCliente) {
		this.editarBlockCliente = editarBlockCliente;
	}

	public boolean isFecharAgenda() {
		return fecharAgenda;
	}

	public void setFecharAgenda(boolean fecharAgenda) {
		this.fecharAgenda = fecharAgenda;
	}
	
}
