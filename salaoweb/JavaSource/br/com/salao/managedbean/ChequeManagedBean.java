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
import br.com.salao.modelo.Cheque;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.vo.ChequeVO;
import br.com.salao.servico.FinanceiroServico;

@ManagedBean(name = "chequeMB")
@ViewScoped
public class ChequeManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	private List<ChequeVO> cheques;
	private List<Cheque> listCheques;
	private ChequeVO cheque;
	private Date dataInicio;
	private Date dataFinal;
	private boolean exibirChequesPendentes;
	private boolean bloquearCalendario;
	private String total;
	
	public ChequeManagedBean(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		cheque = new ChequeVO();
		cheques = new ArrayList<ChequeVO>();	
		total = "";
		exibirChequesPendentes = true;
		bloquearCalendario = true;
	}
	
	@PostConstruct
	public void iniciar(){
		listarCheques();
	}
	
	public void bloquearDesbloquear(){
		if(exibirChequesPendentes){
			bloquearCalendario = true;
		}else{
			bloquearCalendario = false;
		}
	}
	
	public void confirmarPagamento(){
		Cheque cheque = new Cheque();
		cheque.setId(this.cheque.getId());
		cheque = listCheques.get(listCheques.indexOf(cheque));
		try {
			financeiroServico.resolverCheque(cheque);
			listarCheques();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Resolver Cheque", "Sucesso");
		}
		finally{
			this.cheque = new ChequeVO();
		}
	}
	
	public void listarCheques(){
		try{
			if(exibirChequesPendentes){
				listCheques = financeiroServico.buscarChequesPendentes();
			}else{
				if(!intervaloValido(dataInicio, dataFinal, "dia")){
					addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato Profissional", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
					return;
				}
				listCheques = financeiroServico.buscarCheques(dataInicio, dataFinal);
			}
			this.cheques.clear();
			Float total = 0f;
			for(Cheque ch : listCheques){
				ChequeVO cheque = new ChequeVO();
				cheque.setId(ch.getId());
				cheque.setBanco(ch.getBanco().getNome());
				cheque.setData(ch.getData());
				cheque.setDataPagamento(ch.getDataPagamento());
				cheque.setValor(ch.getValor());
				total += ch.getValor();
				List<Item> itens = new ArrayList<Item>();
				itens.addAll(ch.getComanda().getItens());
				cheque.setCliente(itens.get(0).getCliente().getNome());
				this.cheques.add(cheque);
			}
			NumberFormat nFormat = NumberFormat.getCurrencyInstance();					
			this.total = nFormat.format(total);		
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato",e.getMessage());
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

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public List<ChequeVO> getCheques() {
		return cheques;
	}

	public void setCheques(List<ChequeVO> cheques) {
		this.cheques = cheques;
	}

	public ChequeVO getCheque() {
		return cheque;
	}

	public void setCheque(ChequeVO cheque) {
		this.cheque = cheque;
	}

	public boolean isExibirChequesPendentes() {
		return exibirChequesPendentes;
	}

	public void setExibirChequesPendentes(boolean exibirChequesPendentes) {
		this.exibirChequesPendentes = exibirChequesPendentes;
	}

	public boolean isBloquearCalendario() {
		return bloquearCalendario;
	}

	public void setBloquearCalendario(boolean bloquearCalendario) {
		this.bloquearCalendario = bloquearCalendario;
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
