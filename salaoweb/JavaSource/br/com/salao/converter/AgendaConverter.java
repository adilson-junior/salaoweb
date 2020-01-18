package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Agenda;

@FacesConverter(value = "agendaConverter")
public class AgendaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Agenda agenda = new Agenda();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Agenda();
		}
		agenda.setId(new Integer(campos[0]));		
		return agenda;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Agenda agenda = (Agenda) valor;
		if (agenda.getId() == null || agenda.getId() == 0) {
			return "";
		}
		return "" + agenda.getId() + "-" + agenda.getCliente().getNome();
	}
}