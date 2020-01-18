package br.com.salao.sessao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.salao.managedbean.StatusManagedBean;

/**
 * Servlet Filter implementation class SessaoFilter
 */
@WebFilter("/pg/*")
public class SessaoFilter implements Filter {
	
	private List<String> urls;
	
    /**
     * Default constructor. 
     */
    public SessaoFilter() {     	
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Object objUsuario = ((HttpServletRequest)request).getSession().getAttribute("usuario");	
		Object managedBean = ((HttpServletRequest)request).getSession().getAttribute("statusMB");	
		String paginaEvento = ((HttpServletRequest)request).getRequestURI();
		//String contextPath = ((HttpServletRequest)request).getContextPath();
		int index = urls.indexOf(paginaEvento);
		String pag = "";
		if(index >= 0){
			pag = urls.get(index);
		}		
		if (objUsuario == null){					
			String[] strs = ((HttpServletRequest)request).getRequestURL().toString().split("/");				
			((HttpServletResponse)response).sendRedirect("http://"+strs[2]+"/"+strs[3]+"/login.jsf");			
		} if (managedBean != null && !((StatusManagedBean)managedBean).getCaixaAberto() && paginaEvento.equals(pag)){		
			String[] strs = ((HttpServletRequest)request).getRequestURL().toString().split("/");				
			((HttpServletResponse)response).sendRedirect("http://"+strs[2]+"/"+strs[3]+"/pg/caixa/status.jsf");			
		} else {	
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {		
		String contextPath = fConfig.getServletContext().getContextPath();
		urls = new ArrayList<String>();
    	urls.add(contextPath+"/pg/comanda.jsf");
    	urls.add(contextPath+"/pg/caixa/saldo.jsf");
    	urls.add(contextPath+"/pg/caixa/extrato.jsf");
    	urls.add(contextPath+"/pg/caixa/entrada-saida.jsf"); 	
	}

}
