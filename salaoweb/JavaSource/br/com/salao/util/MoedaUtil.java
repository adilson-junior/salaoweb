package br.com.salao.util;

import java.text.NumberFormat;

public class MoedaUtil {
	
	public String formatarMoeda(Number valor){
		NumberFormat nFormat = NumberFormat.getCurrencyInstance();		
		return nFormat.format(valor);
	}

}
