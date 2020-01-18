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
import br.com.salao.modelo.Cliente;
import br.com.salao.modelo.Comanda;
import br.com.salao.modelo.Item;
import br.com.salao.modelo.vo.ExtratoVO;
import br.com.salao.servico.ClienteServico;
import br.com.salao.servico.FinanceiroServico;

@ManagedBean(name = "hGeralMB")
@ViewScoped
public class HistoricoGeralManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	@ManagedProperty(name="clienteServico",value="#{clienteServico}")
	private ClienteServico clienteServico;
	private Cliente cliente;
	private Date dataInicio;
	private Date dataFinal;
	private List<ExtratoVO> extratos;
	private List<Cliente> clientes;	
	private String totalValor;
	private String totalDesconto;
	private String totalComanda;
	
	public HistoricoGeralManagedBean(){
		extratos = new ArrayList<ExtratoVO>();
		totalValor = "";
		totalDesconto = "";
		totalComanda = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
	}
	
	@PostConstruct
	public void iniciar(){
		calcularExtrato();
		listarClientes();
	}
	
	public void calcularExtrato(){
		if(!intervaloValido(dataInicio, dataFinal, "dia")){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Histórico", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
			return;
		}
		try {			
			List<Comanda> comandas = null;
			extratos.clear();
			if(cliente == null || cliente.getId() == null){
				comandas = financeiroServico.buscarComandas(dataInicio, dataFinal);
			}else{
				comandas = financeiroServico.buscarComandas(dataInicio, dataFinal, cliente.getId());
			}
			Float totalValor = 0f;
			Float totalDesconto2 = 0f;
			Float totalComanda = 0f;
			for(Comanda com : comandas){
				String nomeCliente = null;				
				Float totalVenda = 0F;
				Float totalDesconto = 0F;				
				for(Item it : com.getItens()){
					if(nomeCliente == null){
						nomeCliente = it.getCliente().getNome();
					}
					totalVenda += (it.getValor() * it.getQuantidade());
					totalDesconto += it.getDesconto();
					totalValor += (it.getValor() * it.getQuantidade());
					totalDesconto2 += it.getDesconto();					
				}
				ExtratoVO extrato = new ExtratoVO();
				extrato.setData(com.getDataPagamento());
				extrato.setCliente(nomeCliente);
				if(totalDesconto > 0){
					extrato.setDescontoTotalComanda(totalDesconto * -1);					
				}else{
					extrato.setDescontoTotalComanda(totalDesconto);
				}
				extrato.setTotal(totalVenda - totalDesconto);
				extrato.setValorTotalComanda(totalVenda);				
				extratos.add(extrato);
				totalComanda = totalValor - totalDesconto2;
				NumberFormat nFormat = NumberFormat.getCurrencyInstance();
				this.totalComanda = nFormat.format(totalComanda);
				this.totalValor = nFormat.format(totalValor);
				if(totalDesconto2 > 0){
					this.totalDesconto = nFormat.format(totalDesconto2 * -1);
				}else{
					this.totalDesconto = nFormat.format(totalDesconto2);
				}					
			}
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Extrato",e.getMessage());
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
	
	private void listarClientes(){
		try {
			clientes = null;
			clientes = clienteServico.listar(true);
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
	
	public FinanceiroServico getFinanceiroServico() {
		return financeiroServico;
	}
	public void setFinanceiroServico(FinanceiroServico financeiroServico) {
		this.financeiroServico = financeiroServico;
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
	
	public List<ExtratoVO> getExtratos() {
		return extratos;
	}

	public void setExtratos(List<ExtratoVO> extratos) {
		this.extratos = extratos;
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

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public String getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(String totalValor) {
		this.totalValor = totalValor;
	}

	public String getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(String totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public String getTotalComanda() {
		return totalComanda;
	}

	public void setTotalComanda(String totalComanda) {
		this.totalComanda = totalComanda;
	}	
}
