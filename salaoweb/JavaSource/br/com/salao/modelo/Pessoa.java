package br.com.salao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@SequenceGenerator(name="pessoa_seq", sequenceName="pessoa_seq", allocationSize=1)
public abstract class Pessoa implements Serializable, Comparable<Pessoa>{
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pessoa_seq")
	private Integer id;	
	@Column(nullable=false,columnDefinition="varchar(150)")
	private String nome;
	@Column(nullable=true,columnDefinition="varchar(14)")
	private String telefone;
	@Column(nullable=true,columnDefinition="varchar(15)")
	private String celular;
	@Column(nullable=true,columnDefinition="varchar(5)")
	private String aniversario;
	@Column(nullable=true,columnDefinition="varchar(100)")
	private String email;
	@Column(nullable=true,columnDefinition="varchar")
	private String endereco;
	@Column(nullable=true,columnDefinition="varchar(10)")
	private String cep;
	@Column(nullable=true,columnDefinition="varchar")
	private String bairro;
	@Column(nullable=true,columnDefinition="varchar")
	private String cidade;
	@Column(nullable=true,columnDefinition="varchar")
	private String estado;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean ativo;	
	
	public Pessoa() {	
		ativo = new Boolean(true);
	}
	
	public String getNome() {		
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCelular() {
		return celular == null ? telefone : celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getAniversario() {
		return aniversario;
	}

	public void setAniversario(String aniversario) {
		this.aniversario = aniversario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {		
		return nome;
	}	
	
	@Override
	public int compareTo(Pessoa o) {		
        return this.id.compareTo(o.getId());
	}	
}
