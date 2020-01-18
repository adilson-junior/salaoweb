package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.ContaPag;
import br.com.salao.modelo.Usuario;
import br.com.salao.servico.AgendaServico;
import br.com.salao.servico.ContaPagServico;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.util.DateUtil;

@ManagedBean(name="statusMB")
@SessionScoped
public class StatusManagedBean extends BaseBean{	
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name="financeiroServico",value="#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(name="agendaServico",value="#{agendaServico}")
	private AgendaServico agendaServico;
	@ManagedProperty(value="#{loginMB}")
	private LoginManagedBean loginManagedBean;
	@ManagedProperty(name="contaPagServico",value="#{contaPagServico}")
	private ContaPagServico contaPagServico;	
	private List<Caixa> caixas;	
	private List<ContaPag> contasAtrasadas;
	private List<ContaPag> contasAPagar;
	private int qtdContasAPagar;
	private int qtdContasAtrasadas;
	private long qtdAgendas;
	private String statusCaixa;
	private String usuarioAbertura;
	private String corBackground;
	private String corTexto;
	private Caixa caixaPendente;
	private boolean exibirAbrirCaixa;
	private boolean exibirFecharCaixa;
	private boolean exibirFecharCaixaPendente;
	private Date dataUltimaContaVencer;
	
	public StatusManagedBean(){		
		caixas = new ArrayList<Caixa>();
		statusCaixa = "Abrir o caixa";
		corBackground = "#0040FF";//Azul	
		corTexto = "#ffffff";
		exibirAbrirCaixa = true;
		exibirFecharCaixa = false;
		exibirFecharCaixaPendente = false;	
		contasAtrasadas = new ArrayList<ContaPag>();
		contasAPagar = new ArrayList<ContaPag>();
		dataUltimaContaVencer = new Date();
	}
	
	@PostConstruct
	public void iniciar(){
		validarCaixa();			
	}	
	
	public void carregar(){
		listarContas();
		quantidadeAgendas();
	}
	
	private void listarContas(){
		qtdContasAtrasadas = 0;
		qtdContasAPagar = 0;
		try{
			contasAPagar.clear();
			contasAtrasadas.clear();
			Calendar cal = Calendar.getInstance();				
			cal.add(Calendar.DAY_OF_MONTH, 6);
			dataUltimaContaVencer = cal.getTime();
			contasAtrasadas = contaPagServico.listarAtrasadas();
			qtdContasAtrasadas = contasAtrasadas.size();
			contasAPagar = contaPagServico.listarAVencer();
			qtdContasAPagar = contasAPagar.size();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());
		}		
	}

	public void validarCaixa(){
		try {
			caixas = financeiroServico.buscarCaixaAberto();
			DateUtil dateUtil = new DateUtil();
			Date hoje = dateUtil.getDataHora(new Date(), 0, 0, 0);					
			if(caixas.size() == 0){
				exibirAbrirCaixa = true;
				exibirFecharCaixa = false;
				exibirFecharCaixaPendente = false;
				statusCaixa = "Abrir o caixa";
				corBackground = "#0040FF";//Azul	
				usuarioAbertura = "";
				return;
			}
			for (Caixa cx : caixas){
				Date abertura = dateUtil.getDataHora(cx.getDataAbertura(), 0, 0, 0);
				if (abertura.before(hoje)){
					statusCaixa = "O caixa do dia "+dateUtil.formatarParaData(abertura)+" não foi fechado.";
					usuarioAbertura = "Aberto em "+dateUtil.formartarDataHora(cx.getDataAbertura())+" por "+cx.getUsuarioAbertura();
					corBackground = "#FE2E2E";//Vermelho
					corTexto = "#ffffff";	
					caixaPendente = cx;
					exibirAbrirCaixa = false;
					exibirFecharCaixa = false;
					exibirFecharCaixaPendente = true;
					break;
				}
				if (abertura.getTime() == hoje.getTime() && cx.getStatus() == 1){					
					statusCaixa = "Caixa aberto: "+dateUtil.formartarDataHora(cx.getDataAbertura());
					usuarioAbertura = "Usuário: "+cx.getUsuarioAbertura();					
					if(loginManagedBean != null){
						Usuario usuario = loginManagedBean.getUsuario();
						if(usuario != null && !usuario.getNomeLogin().equals(cx.getUsuarioAbertura())){
							corBackground = "#FE2E2E";//Vermelho
							corTexto = "#ffffff";	
							caixaPendente = cx;
							exibirAbrirCaixa = false;
							exibirFecharCaixa = false;
							exibirFecharCaixaPendente = true;
						}else{
							corBackground = "#3ADF00";//Verde
							corTexto = "#0101DF";		
							caixaPendente = cx;
							exibirAbrirCaixa = false;
							exibirFecharCaixa = true;
							exibirFecharCaixaPendente = false;
						}
					}else{
						corBackground = "#3ADF00";//Verde
						corTexto = "#0101DF";		
						caixaPendente = cx;
						exibirAbrirCaixa = false;
						exibirFecharCaixa = true;
						exibirFecharCaixaPendente = false;
					}					
				}
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());
		}
	}	
	
	public boolean getCaixaAberto(){
		validarCaixa();
		if(exibirAbrirCaixa || exibirFecharCaixaPendente){
			return false;
		}
		return true;
	}
	
	private void quantidadeAgendas(){
		try {
			qtdAgendas = agendaServico.buscarQunatidade(new Date());
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());
		}
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
	}

	public List<Caixa> getCaixas() {
		return caixas;
	}

	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public long getQtdAgendas() {
		return qtdAgendas;
	}

	public void setQtdAgendas(long qtdAgendas) {
		this.qtdAgendas = qtdAgendas;
	}

	public AgendaServico getAgendaServico() {
		return agendaServico;
	}

	public void setAgendaServico(AgendaServico agendaServico) {
		this.agendaServico = agendaServico;
	}

	public String getStatusCaixa() {
		return statusCaixa;
	}

	public void setStatusCaixa(String statusCaixa) {
		this.statusCaixa = statusCaixa;
	}

	public String getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(String usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public String getCorBackground() {
		return corBackground;
	}

	public void setCorBackground(String corBackground) {
		this.corBackground = corBackground;
	}

	public String getCorTexto() {
		return corTexto;
	}

	public void setCorTexto(String corTexto) {
		this.corTexto = corTexto;
	}

	public boolean isExibirAbrirCaixa() {
		return exibirAbrirCaixa;
	}

	public void setExibirAbrirCaixa(boolean exibirAbrirCaixa) {
		this.exibirAbrirCaixa = exibirAbrirCaixa;
	}

	public boolean isExibirFecharCaixa() {
		return exibirFecharCaixa;
	}

	public void setExibirFecharCaixa(boolean exibirFecharCaixa) {
		this.exibirFecharCaixa = exibirFecharCaixa;
	}

	public boolean isExibirFecharCaixaPendente() {
		return exibirFecharCaixaPendente;
	}

	public void setExibirFecharCaixaPendente(boolean exibirFecharCaixaPendente) {
		this.exibirFecharCaixaPendente = exibirFecharCaixaPendente;
	}

	public Caixa getCaixaPendente() {
		return caixaPendente;
	}

	public LoginManagedBean getLoginManagedBean() {
		return loginManagedBean;
	}

	public void setLoginManagedBean(LoginManagedBean loginManagedBean) {
		this.loginManagedBean = loginManagedBean;
	}

	public ContaPagServico getContaPagServico() {
		return contaPagServico;
	}

	public void setContaPagServico(ContaPagServico contaPagServico) {
		this.contaPagServico = contaPagServico;
	}	

	public int getQtdContasAPagar() {
		return qtdContasAPagar;
	}

	public void setQtdContasAPagar(int qtdContasAPagar) {
		this.qtdContasAPagar = qtdContasAPagar;
	}

	public int getQtdContasAtrasadas() {
		return qtdContasAtrasadas;
	}

	public void setQtdContasAtrasadas(int qtdContasAtrasadas) {
		this.qtdContasAtrasadas = qtdContasAtrasadas;
	}

	public List<ContaPag> getContasAtrasadas() {
		return contasAtrasadas;
	}

	public void setContasAtrasadas(List<ContaPag> contasAtrasadas) {
		this.contasAtrasadas = contasAtrasadas;
	}

	public List<ContaPag> getContasAPagar() {
		return contasAPagar;
	}

	public void setContasAPagar(List<ContaPag> contasAPagar) {
		this.contasAPagar = contasAPagar;
	}

	public Date getDataUltimaContaVencer() {
		return dataUltimaContaVencer;
	}

	public void setDataUltimaContaVencer(Date dataUltimaContaVencer) {
		this.dataUltimaContaVencer = dataUltimaContaVencer;
	}	
}
