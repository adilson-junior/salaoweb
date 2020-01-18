package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Cliente;

@FacesConverter(value = "clienteConverter")
public class ClienteConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor != null && !valor.isEmpty()) {
            return (Cliente) ui.getAttributes().get(valor);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor instanceof Cliente) {
            Cliente entity= (Cliente) valor;
            if (entity != null && entity instanceof Cliente && entity.getId() != null) {
                ui.getAttributes().put( entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
	}
}