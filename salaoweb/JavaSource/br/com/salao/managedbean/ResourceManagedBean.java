package br.com.salao.managedbean;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.salao.servico.BancoServico;
import br.com.salao.servico.BandeiraServico;
import br.com.salao.servico.CategoriaServico;
import br.com.salao.servico.ClienteServico;
import br.com.salao.servico.ContaPagServico;
import br.com.salao.servico.EntradaSaidaServico;
import br.com.salao.servico.FinanceiroServico;
import br.com.salao.servico.ProdutoServico;
import br.com.salao.servico.ProfissionalServico;
import br.com.salao.servico.ServicoServico;
import br.com.salao.servico.UsuarioServico;

@ManagedBean(name="resourceMB", eager=true)
@ViewScoped
public class ResourceManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private static Map<String, Object> mapServicos;
	@ManagedProperty(name="clienteServico",value="#{clienteServico}")
	private ClienteServico clienteServico;
	@ManagedProperty(name="profissionalServico",value="#{profissionalServico}")
	private ProfissionalServico profissionalServico;
	@ManagedProperty(name="bandeiraServico",value="#{bandeiraServico}")
	private BandeiraServico bandeiraServico;
	@ManagedProperty(name="categoriaServico",value="#{categoriaServico}")
	private CategoriaServico categoriaServico;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(name="contaPagServico",value="#{contaPagServico}")
	private ContaPagServico contaPagServico;
	@ManagedProperty(name = "entradaSaidaServico", value = "#{entradaSaidaServico}")
	private EntradaSaidaServico entradaSaidaServico;
	@ManagedProperty(name="produtoServico",value="#{produtoServico}")
	private ProdutoServico produtoServico;
	@ManagedProperty(name="servicoServico",value="#{servicoServico}")
	private ServicoServico servicoServico;
	@ManagedProperty(name="usuarioServico",value="#{usuarioServico}")
	private UsuarioServico usuarioServico;	
	@ManagedProperty(name="bancoServico",value="#{bancoServico}")
	private BancoServico bancoServico;
	
	static{
		mapServicos = new HashMap<String, Object>();
	}
	
	public ResourceManagedBean(){		
	}	
	
	public void iniciar(){		
	}
	
	public static Object getServico(String nome){
		return mapServicos.get(nome);
	}
	
	public ClienteServico getClienteServico() {
		return clienteServico;
	}
	
	public void setClienteServico(ClienteServico clienteServico) {
		this.clienteServico = clienteServico;
		mapServicos.put("clienteServico", this.clienteServico);
	}

	public ProfissionalServico getProfissionalServico() {
		return profissionalServico;
	}

	public void setProfissionalServico(ProfissionalServico profissionalServico) {
		this.profissionalServico = profissionalServico;		
		mapServicos.put("profissionalServico", this.profissionalServico);
	}

	public BandeiraServico getBandeiraServico() {
		return bandeiraServico;
	}

	public void setBandeiraServico(BandeiraServico bandeiraServico) {
		this.bandeiraServico = bandeiraServico;
		mapServicos.put("bandeiraServico", this.bandeiraServico);
	}

	public CategoriaServico getCategoriaServico() {
		return categoriaServico;
	}

	public void setCategoriaServico(CategoriaServico categoriaServico) {
		this.categoriaServico = categoriaServico;
		mapServicos.put("categoriaServico", this.categoriaServico);
	}

	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}

	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
		mapServicos.put("financeiroServico", this.financeiroServico);
	}

	public ContaPagServico getContaPagServico() {
		return contaPagServico;
	}

	public void setContaPagServico(ContaPagServico contaPagServico) {
		this.contaPagServico = contaPagServico;
		mapServicos.put("contaPagServico", this.contaPagServico);
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
		mapServicos.put("produtoServico", this.produtoServico);
	}

	public ServicoServico getServicoServico() {
		return servicoServico;
	}

	public void setServicoServico(ServicoServico servicoServico) {
		this.servicoServico = servicoServico;
		mapServicos.put("servicoServico", this.servicoServico);
	}

	public UsuarioServico getUsuarioServico() {
		return usuarioServico;
	}

	public void setUsuarioServico(UsuarioServico usuarioServico) {
		this.usuarioServico = usuarioServico;
		mapServicos.put("usuarioServico", this.usuarioServico);
	}

	public EntradaSaidaServico getEntradaSaidaServico() {
		return entradaSaidaServico;
	}

	public void setEntradaSaidaServico(EntradaSaidaServico entradaSaidaServico) {
		this.entradaSaidaServico = entradaSaidaServico;
		mapServicos.put("entradaSaidaServico", this.entradaSaidaServico);
	}

	public BancoServico getBancoServico() {
		return bancoServico;
	}

	public void setBancoServico(BancoServico bancoServico) {
		this.bancoServico = bancoServico;
		mapServicos.put("bancoServico", this.bancoServico);
	}

}
