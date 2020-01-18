package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Profissional;

@FacesConverter(value = "profissionalConverter")
public class ProfissionalConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Profissional profissional = new Profissional();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Profissional();
		}
		profissional.setId(new Integer(campos[0]));
		profissional.setNome(campos[1]);
		return profissional;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Profissional profissional = (Profissional) valor;
		if (profissional.getId() == null || profissional.getId() == 0) {
			return "";
		}
		return "" + profissional.getId() + "-" + profissional.getNome();
	}
}