package br.com.salao.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@SequenceGenerator(name="usuario_seq", sequenceName="usuario_seq", allocationSize=1)
public class Usuario implements Serializable, Comparable<Usuario>{	
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="usuario_seq")
	private Integer id;
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String nome;
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String nomeLogin;
	@Column(nullable=false,columnDefinition="varchar(6)")
	private String senha;	
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean ativo;
	@Column(nullable=false,columnDefinition="boolean")
	private Boolean admin;
	
	public Usuario(){		
		setAtivo(new Boolean(true));
		setAdmin(new Boolean(false));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeLogin() {
		return nomeLogin;
	}

	public void setNomeLogin(String nomeLogin) {
		this.nomeLogin = nomeLogin;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
	@Override
	public int compareTo(Usuario o) {		
        return this.id.compareTo(o.getId());
	}
	
	@Override
	public String toString() {
		return nome;
	}	
}
