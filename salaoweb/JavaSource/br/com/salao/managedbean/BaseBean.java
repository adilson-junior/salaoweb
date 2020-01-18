package br.com.salao.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public BaseBean() {
	}

	/**
	 * Mensagem de tela
	 * 
	 * @param serverity
	 *            - (ERROR, FATAL, WARN, INFO)
	 * @param titulo
	 *            - Titulo da mensagem
	 * @param msg
	 *            - Detalhes da mensagem
	 */
	protected void addMensagem(Severity serverity, String titulo, String msg) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(serverity, titulo, msg));
	}

	/**
	 * Redireciona
	 * 
	 * @param pagina
	 *            url da página
	 */
	protected void redirecionar(String pagina) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			String path = facesContext.getExternalContext().getContextName();		
			facesContext.getExternalContext().redirect("/"+path+"/"+pagina);
		} catch (IOException e) {
			addMensagem(FacesMessage.SEVERITY_ERROR, "xxx", "xxxx");
		}
	}

	protected HttpServletRequest getRequest() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (HttpServletRequest) context.getExternalContext().getRequest();
	}

	protected Map<String, Object> getRequestMap() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getRequestMap();
	}

	protected void addAtributoSessao(String nome, Object valor) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		session.setAttribute(nome, valor);
	}
	
	protected ServletContext getServletContext(){
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
}
