package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Profissional;
import br.com.salao.servico.AgendaServico;
import br.com.salao.servico.ProfissionalServico;

@ManagedBean(name="magendaMB")
@ViewScoped
public class MAgendaManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;	
	private List<Agenda> agendas;	
	private Agenda agendaSelecionada;
	@ManagedProperty(name="agendaServico",value="#{agendaServico}")
	private AgendaServico agendaServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(value="#{mloginMB}")
	private MLoginManagedBean mLoginManagedBean;
	private Date dataSelecionada;	
	private int qtdAgedas;
	private Profissional profissional;	
	
	public MAgendaManagedBean(){
		agendas = new ArrayList<Agenda>();	
		setDataSelecionada(new Date());		
	}
	
	@PostConstruct
	public void iniciar(){
		carregar();
	}	
	
	public void carregar(){				
		try {			
			profissional = mLoginManagedBean.getProfissional();		
			agendas = agendaServico.buscarAgendaProfissional(dataSelecionada, profissional.getId());
			Collections.sort(agendas, new Comparator<Agenda>(){
				@Override
				public int compare(Agenda a1, Agenda a2) {					
					return a1.getHoraDe().compareTo(a2.getHoraDe());
				}				
			});
			qtdAgedas = agendas.size();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public void carregarDataSelecionada(){
		try {		
			agendas = agendaServico.buscarAgendaProfissional(dataSelecionada, profissional.getId());
			Collections.sort(agendas, new Comparator<Agenda>(){
				@Override
				public int compare(Agenda a1, Agenda a2) {					
					return a1.getHoraDe().compareTo(a2.getHoraDe());
				}				
			});
			qtdAgedas = agendas.size();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
		}
	}
	
	public String exibirDetalhes(){
		return "pm:servicos";
	}	

	public List<Agenda> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<Agenda> agendas) {
		this.agendas = agendas;
	}

	public Date getDataSelecionada() {
		return dataSelecionada;
	}

	public void setDataSelecionada(Date dataSelecionada) {
		this.dataSelecionada = dataSelecionada;
	}

	public int getQtdAgedas() {
		return qtdAgedas;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public AgendaServico getAgendaServico() {
		return agendaServico;
	}

	public void setAgendaServico(AgendaServico agendaServico) {
		this.agendaServico = agendaServico;
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public Agenda getAgendaSelecionada() {
		return agendaSelecionada;
	}

	public void setAgendaSelecionada(Agenda agendaSelecionada) {
		this.agendaSelecionada = agendaSelecionada;
	}

	public MLoginManagedBean getmLoginManagedBean() {
		return mLoginManagedBean;
	}

	public void setmLoginManagedBean(MLoginManagedBean mLoginManagedBean) {
		this.mLoginManagedBean = mLoginManagedBean;
	}	
	
}
