package br.com.salao.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	public DateUtil() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

	/**
	 * Retorna uma data com formato mm/dd/yyyy HH:mm:ss do dia atual
	 * 
	 * @param hora
	 * @param min
	 * @param seg
	 * @return Date
	 */
	public Date getDataHora(int hora, int min, int seg) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, seg);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public Date getDataHora(Date data, int hora, int min, int seg){
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, seg);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 
	 * @param data
	 * @return data formatada dd/MM/yyyy
	 */
	public String formatarParaData(Date data) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String strData = sdf.format(data);			
		return strData;
	}
	
	public String formatarParaDataSQL(Date data) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strData = sdf.format(data);			
		return strData;
	}
	
	 public String formartarDataHora(Date data){
		 if(data == null){
		 return "";
		 }
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 String strData = format.format(data);
		 return strData;
	 }
	
	/**
	 * 
	 * @param data
	 * @return data formatada hh:mm
	 */
	public String formatarParaHora(Date data) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String strData = sdf.format(data);			
		return strData;
	}
	
	public String formartarMinutosEmHora(int minutos){
		int h = minutos / 60;
		int m = minutos % 60;
		String hora = h < 10 ? "0"+h : ""+h;
		String minuto = m < 10 ? "0"+m : ""+m;
		return hora+":"+minuto;
	}

	/**
	 * 
	 * @param mes
	 * @return nome do mês
	 */
	public String getNomeMes(int mes) {
		String nome = "";
		switch (mes) {
		case 1:
			nome = "Jan";
			break;
		case 2:
			nome = "Fev";
			break;
		case 3:
			nome = "Mar";
			break;
		case 4:
			nome = "Abr";
			break;
		case 5:
			nome = "Mai";
			break;
		case 6:
			nome = "Jun";
			break;
		case 7:
			nome = "Jul";
			break;
		case 8:
			nome = "Ago";
			break;
		case 9:
			nome = "Set";
			break;
		case 10:
			nome = "Out";
			break;
		case 11:
			nome = "Nov";
			break;
		case 12:
			nome = "Dez";
			break;
		}
		return nome;
	}
}