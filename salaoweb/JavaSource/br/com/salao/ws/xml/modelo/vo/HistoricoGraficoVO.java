package br.com.salao.ws.xml.modelo.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class HistoricoGraficoVO implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	private String nome;
	private Float valor;
	
	public HistoricoGraficoVO(){		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
}
