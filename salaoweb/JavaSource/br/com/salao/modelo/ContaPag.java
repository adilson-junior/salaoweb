package br.com.salao.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@SequenceGenerator(name="contapag_seq", sequenceName="contapag_seq", allocationSize=1)
public class ContaPag implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="contapag_seq")
	private Integer id;
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String descricao;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date mesAnoReferencia;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	@Column(nullable=true)
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	@Column(nullable=false,columnDefinition="numeric")
	private Float valor;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean pago;
	
	public ContaPag(){		
		pago = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getMesAnoReferencia() {
		return mesAnoReferencia;
	}

	public void setMesAnoReferencia(Date mesAnoReferencia) {
		this.mesAnoReferencia = mesAnoReferencia;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

}
