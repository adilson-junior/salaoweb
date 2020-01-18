package br.com.salao.managedbean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Reforco;
import br.com.salao.servico.EntradaSaidaServico;

@ManagedBean(name = "reforcoMB")
@ViewScoped
public class ReforcoManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;	
	@ManagedProperty(name = "entradaSaidaServico", value = "#{entradaSaidaServico}")
	private EntradaSaidaServico entradaSaidaServico;	
	private List<Reforco> reforcos;	
	private Date dataInicio;
	private Date dataFinal;
	private String total;
	
	public ReforcoManagedBean(){	
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		reforcos = new ArrayList<Reforco>();
		total = "";
	}
	
	@PostConstruct
	public void iniciar(){		
		listar();		
	}	
	
	public void listar(){
		if(!intervaloValido(dataInicio, dataFinal, "dia")){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Reforço", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
			return;
		}
		try {
			reforcos = entradaSaidaServico.listarReforcos(dataInicio, dataFinal);
			float total = 0;
			for(Reforco rf : reforcos){
				total += rf.getValor();				
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();
			this.total = nFormat.format(total);		
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());		
		}
	}	
	
	private boolean intervaloValido(Date inicio, Date fim, String tipoAgrupamento){
		boolean ok = false;
		int anoMesIni = 0;
		int anoMesFim = 0;
		int intervalo = 0;
		int limite = 0;
		Calendar calendarIni = Calendar.getInstance();
		calendarIni.setTime(inicio);		
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(fim);	
		int mesIni = calendarIni.get(Calendar.MONTH);
		int mesFim = calendarFim.get(Calendar.MONTH);		
		if(tipoAgrupamento.equals("dia")){
			long dif = calendarFim.getTimeInMillis() - calendarIni.getTimeInMillis();	
			anoMesIni = mesIni;
			anoMesFim = mesFim;
			intervalo = (int)(dif / 86400000)+1;
			limite = 30;
		}else{// Mês
			anoMesIni = calendarIni.get(Calendar.YEAR);			
			anoMesFim = calendarFim.get(Calendar.YEAR);
			intervalo = (Math.abs((mesIni + 1) - 13)) + (mesFim + 1);
			limite = 12;
		}		
		if(anoMesIni == anoMesFim){
			ok = true;
		}else if(anoMesIni < anoMesFim && intervalo <= limite){
			ok = true;
		}		
		return ok;
	}

	public EntradaSaidaServico getEntradaSaidaServico() {
		return entradaSaidaServico;
	}

	public void setEntradaSaidaServico(EntradaSaidaServico entradaSaidaServico) {
		this.entradaSaidaServico = entradaSaidaServico;
	}
	
	public List<Reforco> getReforcos() {
		return reforcos;
	}

	public void setReforcos(List<Reforco> reforcos) {
		this.reforcos = reforcos;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}	
	
}
