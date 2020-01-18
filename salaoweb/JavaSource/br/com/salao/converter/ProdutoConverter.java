package br.com.salao.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.salao.modelo.Produto;

@FacesConverter(value = "produtoConverter")
public class ProdutoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent ui, String valor) {
		if (valor == null || valor.equals("")) {
			return valor;
		}	
		Produto produto = new Produto();
		String[] campos = valor.split("-");
		if (campos.length == 1) {
			return new Produto();
		}
		produto.setId(new Integer(campos[0]));
		produto.setNome(campos[1]);
		return produto;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent ui, Object valor) {
		if (valor == null || valor.equals("")) {
			return "";
		}
		Produto produto = (Produto) valor;
		if (produto.getId() == null || produto.getId() == 0) {
			return "";
		}
		return "" + produto.getId() + "-" + produto.getNome();
	}
}