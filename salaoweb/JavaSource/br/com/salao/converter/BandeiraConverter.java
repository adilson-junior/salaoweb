package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Bandeira;

@FacesConverter(value = "bandeiraConverter")
public class BandeiraConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Bandeira bandeira = new Bandeira();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Bandeira();
		}
		bandeira.setId(new Integer(campos[0]));
		bandeira.setNome(campos[1]);
		return bandeira;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Bandeira bandeira = (Bandeira) valor;
		if (bandeira.getId() == null || bandeira.getId() == 0) {
			return "";
		}
		return "" + bandeira.getId() + "-" + bandeira.getNome();
	}
}