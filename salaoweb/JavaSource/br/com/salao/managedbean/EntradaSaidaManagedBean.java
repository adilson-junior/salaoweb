package br.com.salao.managedbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.Caixa;
import br.com.salao.modelo.Reforco;
import br.com.salao.modelo.Sangria;
import br.com.salao.modelo.Usuario;
import br.com.salao.modelo.vo.ExtratoVO;
import br.com.salao.servico.EntradaSaidaServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.UsuarioServico;

@ManagedBean(name = "entradaSaidaMB")
@ViewScoped
public class EntradaSaidaManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;	
	@ManagedProperty(name = "entradaSaidaServico", value = "#{entradaSaidaServico}")
	private EntradaSaidaServico entradaSaidaServico;
	@ManagedProperty(value = "#{statusMB}")
	private StatusManagedBean statusManagedBean;
	@ManagedProperty(name="usuarioServico",value="#{usuarioServico}")
	private UsuarioServico usuarioServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	private List<ExtratoVO> extratos;
	private List<Reforco> reforcos;
	private List<Sangria> sangrias;	

	private Reforco reforco;
	private Sangria sangria;
	private Usuario usuarioAUT;
	private String nome;
	private String senha;
	
	public EntradaSaidaManagedBean(){	
		extratos = new ArrayList<ExtratoVO>();
		reforcos = new ArrayList<Reforco>();
		sangrias = new ArrayList<Sangria>();	
		reforco = new Reforco();
		sangria = new Sangria();		
	}
	
	@PostConstruct
	public void iniciar(){		
		listar();		
	}
	
	public void cancelarReforco(){
		reforco = new Reforco();
	}
	
	public void cancelarSangria(){
		sangria = new Sangria();
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
	
	public void salvarReforco(){
		try {
			if(autorizar()){
				Caixa caixa = statusManagedBean.getCaixaPendente();
				reforco.setCaixa(caixa);
				reforco.setData(new Date());
				reforco.setUsuarioAutoriza(usuarioAUT.getNomeLogin());
				entradaSaidaServico.salvarReforco(reforco);
				reforcos.clear();
				reforcos = entradaSaidaServico.listarReforcos(caixa.getId());	
				addMensagem(FacesMessage.SEVERITY_INFO, "Reforço", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Reforço", e.getMessage());
		}
		finally{
			reforco = new Reforco();
			usuarioAUT = new Usuario();
			nome = "";
			senha = "";	
		}
	}	
	
	public void excluirReforco(){
		try {
			if(autorizar()){
				Caixa caixa = statusManagedBean.getCaixaPendente();		
				entradaSaidaServico.excluirReforco(reforco);
				reforcos = entradaSaidaServico.listarReforcos(caixa.getId());	
				addMensagem(FacesMessage.SEVERITY_INFO, "Reforço", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Reforço", e.getMessage());
		}
		finally{
			reforco = new Reforco();
			usuarioAUT = new Usuario();
			nome = "";
			senha = "";	
		}
	}
	
	public void salvarSangria(){
		try {
			if(autorizar()){
				Caixa caixa = statusManagedBean.getCaixaPendente();
				sangria.setCaixa(caixa);
				sangria.setData(new Date());
				sangria.setUsuarioAutoriza(usuarioAUT.getNomeLogin());
				entradaSaidaServico.salvarSangria(sangria);
				sangrias.clear();
				sangrias = entradaSaidaServico.listarSangrias(caixa.getId());	
				addMensagem(FacesMessage.SEVERITY_INFO, "Sangria", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Sangria", e.getMessage());
		}
		finally{
			sangria = new Sangria();
			usuarioAUT = new Usuario();
			nome = "";
			senha = "";	
		}
	}
	
	public void excluirSangria(){
		try {
			if(autorizar()){
				Caixa caixa = statusManagedBean.getCaixaPendente();			
				entradaSaidaServico.excluirSangria(sangria);
				sangrias.clear();
				sangrias = entradaSaidaServico.listarSangrias(caixa.getId());	
				addMensagem(FacesMessage.SEVERITY_INFO, "Sangria", "Sucesso!");
			}			
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Sangria", e.getMessage());
		}
		finally{
			sangria = new Sangria();
			usuarioAUT = new Usuario();
			nome = "";
			senha = "";	
		}
	}		
	
	public void listar(){
		Caixa caixa = statusManagedBean.getCaixaPendente();
		reforcos.addAll(caixa.getReforcos());
		sangrias.addAll(caixa.getSangrias());		
	}	

	public EntradaSaidaServico getEntradaSaidaServico() {
		return entradaSaidaServico;
	}

	public void setEntradaSaidaServico(EntradaSaidaServico entradaSaidaServico) {
		this.entradaSaidaServico = entradaSaidaServico;
	}

	public List<ExtratoVO> getExtratos() {
		return extratos;
	}

	public void setExtratos(List<ExtratoVO> extratos) {
		this.extratos = extratos;
	}

	public List<Reforco> getReforcos() {
		return reforcos;
	}

	public void setReforcos(List<Reforco> reforcos) {
		this.reforcos = reforcos;
	}

	public List<Sangria> getSangrias() {
		return sangrias;
	}

	public void setSangrias(List<Sangria> sangrias) {
		this.sangrias = sangrias;
	}	

	public StatusManagedBean getStatusManagedBean() {
		return statusManagedBean;
	}

	public void setStatusManagedBean(StatusManagedBean statusManagedBean) {
		this.statusManagedBean = statusManagedBean;
	}

	public Reforco getReforco() {
		return reforco;
	}

	public void setReforco(Reforco reforco) {
		this.reforco = reforco;
	}

	public Sangria getSangria() {
		return sangria;
	}

	public void setSangria(Sangria sangria) {
		this.sangria = sangria;
	}	

	public UsuarioServico getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(UsuarioServico usuarioServico) {
		this.usuarioServico = usuarioServico;
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

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;
	}
}
