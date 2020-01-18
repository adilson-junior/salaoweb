package br.com.salao.modelo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
@XmlRootElement
@Entity
@PrimaryKeyJoinColumn(name="pessoa_id")
public class Profissional extends Pessoa {	
	
	private static final long serialVersionUID = 1L;	
	@OneToMany(mappedBy="profissional", fetch=FetchType.LAZY, targetEntity=Agenda.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Agenda> agendas;
	@OneToMany(mappedBy="profissional", fetch=FetchType.LAZY, targetEntity=Vale.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Vale> vales;
	@OneToMany(mappedBy="profissional", fetch=FetchType.LAZY, targetEntity=Outros.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Outros> outros;
	@OneToMany(mappedBy="profissional", fetch=FetchType.LAZY, targetEntity=Pagamento.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Pagamento> pagamentos;	
	@OneToMany(mappedBy="profissional", fetch=FetchType.LAZY, targetEntity=Item.class)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Item> itens;
	@Transient
	private Map<String, String> mapHorarioNomesClientes;	
	@Transient
	private Map<String, String> mapHorarioIdsClientes;
	@Column(nullable=false,columnDefinition="varchar(6)")
	public String senha;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean salarioFixo;
	
	public Profissional(){			
		setAgendas(new TreeSet<Agenda>());
		setVales(new TreeSet<Vale>());
		setPagamentos(new TreeSet<Pagamento>());
		setItens(new TreeSet<Item>());		
		outros = new TreeSet<Outros>();
		salarioFixo = false;
	}	

	public SortedSet<Agenda> getAgendas() {
		return agendas;
	}

	public void setAgendas(SortedSet<Agenda> agendas) {
		this.agendas = agendas;
	}

	public SortedSet<Vale> getVales() {
		return vales;
	}

	public void setVales(SortedSet<Vale> vales) {
		this.vales = vales;
	}

	public SortedSet<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(SortedSet<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	
	public SortedSet<Item> getItens() {
		return itens;
	}

	public void setItens(SortedSet<Item> itens) {
		this.itens = itens;
	}	
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomesClientesPorHorario(String horario){
		// Se não existe um mapa de nomes e horários de clientes, então criar.	
		if(mapHorarioNomesClientes == null){
			carregarMapas();
		}			
		String nomesClientes = mapHorarioNomesClientes.get(horario);
		if(nomesClientes == null){
			nomesClientes = "";
		}
		return nomesClientes;
	}
	
	public SortedSet<Integer> getIdsClientesPorHorario(String horario){
		if(mapHorarioIdsClientes == null){
			carregarMapas();
		}
		SortedSet<Integer> ids = new TreeSet<Integer>();
		String idsClientes = mapHorarioIdsClientes.get(horario);
		if(idsClientes == null){
			return null;
		}
		String strIds[] = idsClientes.split(",");
		for(String id : strIds){
			ids.add(new Integer(id));
		}
		return ids;
	}
	
	private void carregarMapas(){
		Calendar calendar = Calendar.getInstance();
		mapHorarioNomesClientes = new HashMap<String, String>();
		mapHorarioIdsClientes = new HashMap<String, String>();
		// Percorre as agendas para montar o mapa
		for(Agenda ag : agendas){			
			// Calculando o intevalor de horários do cliente				
			long timeDe = ag.getHoraDe().getTime();				
			long timeAte = ag.getHoraAte().getTime();
			long dif = timeAte - timeDe;
			long minutos  = (dif / 60000 ) % 60;
			long horas = dif / 3600000; 
			// Quantidade de horários a cada 30 minutos
			long qtdHorarios = (horas * 60) / 30;
			if(minutos > 0){
				// Corrigindo a quandidade de horários
				qtdHorarios++;
			}
			// Criando o intervalor de hoários
			calendar.setTimeInMillis(timeDe);
			int horaDe = calendar.get(Calendar.HOUR);
			int minutoDe = calendar.get(Calendar.MINUTE);
			calendar.setTimeInMillis(timeAte);				
			for(int i = 0; i < qtdHorarios; i++){
				String strHorario = "";
				if(minutoDe == 00){
					strHorario = horaDe < 10 ? "0"+horaDe+":00" : ""+horaDe+":00";						
					minutoDe = 30;						
				}else{
					strHorario = horaDe < 10 ? "0"+horaDe+":30" : ""+horaDe+":30";
					minutoDe = 0;
					horaDe++;
				}					
				// Se já existe um cliente no horário. então concatenar (cliente encaixado)
				String nomeCliente = mapHorarioNomesClientes.get(strHorario);			
				String strId = mapHorarioIdsClientes.get(strHorario);
				if(nomeCliente != null){
					StringBuilder sb = new StringBuilder("<html>");
					sb.append(nomeCliente).append("<br/>");
					sb.append(ag.getCliente().getNome());
					sb.append("</html>");
					mapHorarioNomesClientes.put(strHorario, sb.toString());
					mapHorarioIdsClientes.put(strHorario, strId+","+ag.getCliente().getId());
				}else{					
					mapHorarioNomesClientes.put(strHorario, ag.getCliente().getNome());
					mapHorarioIdsClientes.put(strHorario, ag.getCliente().getId().toString());
				}
			}
		}		
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getSalarioFixo() {
		return salarioFixo;
	}

	public void setSalarioFixo(Boolean salarioFixo) {
		this.salarioFixo = salarioFixo;
	}

	public SortedSet<Outros> getOutros() {
		return outros;
	}

	public void setOutros(SortedSet<Outros> outros) {
		this.outros = outros;
	}	
}
