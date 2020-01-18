package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.vo.ChequeVO;

@FacesConverter(value = "chequeVOConverter")
public class ChequeVOConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor != null && !valor.isEmpty()) {
            return (ChequeVO) ui.getAttributes().get(valor);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor instanceof ChequeVO) {
            ChequeVO entity= (ChequeVO) valor;
            if (entity != null && entity instanceof ChequeVO && entity.getId() != null) {
                ui.getAttributes().put( entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
	}
}