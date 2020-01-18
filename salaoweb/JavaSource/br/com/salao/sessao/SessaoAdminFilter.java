package br.com.salao.sessao;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.salao.modelo.Usuario;

/**
 * Servlet Filter implementation class SessaoAdminFilter
 */
@WebFilter("/pg/financeiro/*")
public class SessaoAdminFilter implements Filter {	

    /**
     * Default constructor. 
     */
    public SessaoAdminFilter() {       
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
		if (objUsuario != null && !((Usuario)objUsuario).getAdmin()){					
			String[] strs = ((HttpServletRequest)request).getRequestURL().toString().split("/");				
			((HttpServletResponse)response).sendRedirect("http://"+strs[2]+"/"+strs[3]+"/autorizacao.html");			
		} else {	
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {			
	}

}
