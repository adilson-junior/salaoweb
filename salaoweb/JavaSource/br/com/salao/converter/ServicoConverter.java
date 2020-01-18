package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Servico;

@FacesConverter(value = "servicoConverter")
public class ServicoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Servico servico = new Servico();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Servico();
		}
		servico.setId(new Integer(campos[0]));
		servico.setNome(campos[1]);
		return servico;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Servico servico = (Servico) valor;
		if (servico.getId() == null || servico.getId() == 0) {
			return "";
		}
		return "" + servico.getId() + "-" + servico.getNome();
	}
}