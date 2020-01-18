package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Categoria;

@FacesConverter(value = "categoriaConverter")
public class CategoriaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Categoria categoria = new Categoria();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Categoria();
		}
		categoria.setId(new Integer(campos[0]));
		categoria.setNome(campos[1]);
		return categoria;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Categoria categoria = (Categoria) valor;
		if (categoria.getId() == null || categoria.getId() == 0) {
			return "";
		}
		return "" + categoria.getId() + "-" + categoria.getNome();
	}
}