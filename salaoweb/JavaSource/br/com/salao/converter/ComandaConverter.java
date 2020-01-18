package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Comanda;

@FacesConverter(value = "comandaConverter")
public class ComandaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor != null && !valor.isEmpty()) {
            return (Comanda) ui.getAttributes().get(valor);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor instanceof Comanda) {
            Comanda entity= (Comanda) valor;
            if (entity != null && entity instanceof Comanda && entity.getId() != null) {
                ui.getAttributes().put( entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
	}
}