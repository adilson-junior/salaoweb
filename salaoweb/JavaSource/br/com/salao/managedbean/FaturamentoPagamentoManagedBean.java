package br.com.salao.managedbean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.salao.exception.ServicoException;
import br.com.salao.modelo.vo.HistoricoGraficoVO;
import br.com.salao.servico.FinanceiroServico;

@ManagedBean(name = "faturamentoPagMB")
@ViewScoped
public class FaturamentoPagamentoManagedBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "financeiroServico", value = "#{financeiroServico}")
	private FinanceiroServico financeiroServico;
	private BarChartModel barModel;
	private Date dataInicio;
	private Date dataFinal;
	
	public FaturamentoPagamentoManagedBean(){		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio = calendar.getTime();
		dataFinal = new Date();
		barModel = new BarChartModel();			
		barModel.setAnimate(true);
		barModel.setShowDatatip(true);
		barModel.setShowPointLabels(true);
	}
	
	@PostConstruct
	public void iniciar(){		
		graficoFaturamentoMenosPagamentoMes();
	}
	
	public void carregarGrafico(){		
		if(!intervaloValido(dataInicio, dataFinal, "mes")){
			addMensagem(FacesMessage.SEVERITY_ERROR, "Histórico", "Intervalo entre datas inválido. Intervalo máximo de 30 dias ou o máximo do mês.");			
			return;
		}
		graficoFaturamentoMenosPagamentoMes();
	}		
	
	public void graficoFaturamentoMenosPagamentoMes(){
		try {
			barModel.clear();
			List<HistoricoGraficoVO> historicos = financeiroServico.faturamentoMenosPagamentosMes(dataInicio, dataFinal);
			ChartSeries series = new ChartSeries();
			series.setLabel("Faturamento");			
			for(HistoricoGraficoVO hist : historicos){
				series.set(hist.getNome(), hist.getValor());
			}
			barModel.addSeries(series);
			Axis xAxis = barModel.getAxis(AxisType.X);
			Axis yAxis = barModel.getAxis(AxisType.Y);
	        xAxis.setLabel("Mês");
	        yAxis.setLabel("Valor R$");
	        barModel.setLegendPosition("ne");
	        barModel.setTitle("Faturamento menos pagamento mensal");
		} catch (ServicoException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "Gráfico", e.getMessage());	
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

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
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
}
