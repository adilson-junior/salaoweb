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

/**
 * Servlet Filter implementation class MSessaoFilter
 */
@WebFilter("/m/pg/*")
public class MSessaoFilter implements Filter {
	
    /**
     * Default constructor. 
     */
    public MSessaoFilter() {       
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
		Object objProfissional = ((HttpServletRequest)request).getSession().getAttribute("profissional");		
		if (objProfissional == null){					
			String[] strs = ((HttpServletRequest)request).getRequestURL().toString().split("/");				
			((HttpServletResponse)response).sendRedirect("http://"+strs[2]+"/"+strs[3]+"/"+strs[4]+"/login.jsf");			
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
