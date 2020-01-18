package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Banco;

@FacesConverter(value = "bancoConverter")
public class BancoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Banco banco = new Banco();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Banco();
		}
		banco.setId(new Integer(campos[0]));
		banco.setNome(campos[1]);
		return banco;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Banco banco = (Banco) valor;
		if (banco.getId() == null || banco.getId() == 0) {
			return "";
		}
		return "" + banco.getId() + "-" + banco.getNome();
	}
}