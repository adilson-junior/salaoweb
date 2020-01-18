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
import br.com.salao.modelo.Profissional;
import br.com.salao.modelo.Usuario;
import br.com.salao.modelo.Vale;
import br.com.salao.servico.EntradaSaidaServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.UsuarioServico;

@ManagedBean(name = "valeMB")
@ViewScoped
public class ValeManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "entradaSaidaServico", value = "#{entradaSaidaServico}")
	private EntradaSaidaServico entradaSaidaServico;
	@ManagedProperty(name="usuarioServico",value="#{usuarioServico}")
	private UsuarioServico usuarioServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	private List<Profissional> profissionais;
	private List<Vale> vales;
	private Profissional profissional;
	private Profissional profissionalVale;
	private Usuario usuarioAUT;
	private String nome;
	private String senha;
	private Vale vale;
	private Date dataInicio;
	private Date dataFinal;
	private String total;
	
	public ValeManagedBean(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		profissionais = new ArrayList<Profissional>();	
		vales = new ArrayList<Vale>();
		vale = new Vale();
	}
	
	@PostConstruct
	public void iniciar(){
		listar();
		listarProfissionais();
	}
	
	private boolean autorizar() throws ServicoException{
		boolean ok = false;
		usuarioAUT = usuarioServico.buscar(nome, senha);			
		if (!usuarioAUT.getAdmin()){
			addMensagem(FacesMessage.SEVERITY_WARN, "Login", "O usuário não tem permissão para realizar a operação");				
			ok = false;
		}else{
			ok = true;
		}		
		return ok;
	}
	
	public void cancelar(){
		vale = new Vale();
		nome = "";
		senha = "";
	}
	
	public void salvar(){
		try {
			if(autorizar()){				
				vale.setData(new Date());
				vale.setUsuarioAutoriza(usuarioAUT.getNomeLogin());
				vale.setProfissional(profissionalVale);
				entradaSaidaServico.salvarVale(vale);	
				profissionalVale = null;
				listar();			
				addMensagem(FacesMessage.SEVERITY_INFO, "Vale", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Vale", e.getMessage());
		}
		finally{
			vale = new Vale();
			usuarioAUT = new Usuario();			
			nome = "";
			senha = "";	
		}
	}	
	
	public void excluir(){
		try {
			if(autorizar()){				
				entradaSaidaServico.excluirVale(vale);				
				listar();
				addMensagem(FacesMessage.SEVERITY_INFO, "Vale", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Vale", e.getMessage());
		}
		finally{
			vale = new Vale();
			usuarioAUT = new Usuario();
			nome = "";
			senha = "";	
		}
	}
	
	public void listar(){
		if(!intervaloValido(dataInicio, dataFinal, "dia")){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Histórico", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
			return;
		}
		try {
			if(profissional == null){
				vales.clear();
				vales.addAll(entradaSaidaServico.listarVales(dataInicio, dataFinal));
			}else{
				vales.clear();
				vales.addAll(entradaSaidaServico.listarVales(profissional.getId(), dataInicio, dataFinal));
			}			
			calcularTotal();
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar", e.getMessage());
		}	
	}
	
	private void calcularTotal(){
		Float total = 0f;
		for(Vale v : vales){
			total += v.getValor();
		}
		NumberFormat nFormat = NumberFormat.getCurrencyInstance();
		this.total = nFormat.format(total);	
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
	
	public void listarProfissionais(){
		try {
			profissionais = profissionalServico.listar(true);
		} catch (ServicoException e) {			
			addMensagem(FacesMessage.SEVERITY_ERROR, "Listar",e.getMessage());		
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

	public UsuarioServico getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(UsuarioServico usuarioServico) {
		this.usuarioServico = usuarioServico;
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}

	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}

	public Usuario getUsuarioAUT() {
		return usuarioAUT;
	}

	public void setUsuarioAUT(Usuario usuarioAUT) {
		this.usuarioAUT = usuarioAUT;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Vale getVale() {
		return vale;
	}

	public void setVale(Vale vale) {
		this.vale = vale;
	}

	public List<Vale> getVales() {
		return vales;
	}

	public void setVales(List<Vale> vales) {
		this.vales = vales;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Profissional getProfissionalVale() {
		return profissionalVale;
	}

	public void setProfissionalVale(Profissional profissionalVale) {
		this.profissionalVale = profissionalVale;
	}	
}
