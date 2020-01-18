package br.com.salao.util;

import java.text.SimpleDateFormat;


import br.com.salao.modelo.Agenda;
import br.com.salao.modelo.Cliente;
import br.com.salao.modelo.Profissional;

public class Correio {
	
	public void enviar(Agenda agenda, Cliente cliente, Profissional profissional) throws Exception{
	    SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");	
	    SimpleDateFormat horaFormatada = new SimpleDateFormat("HH:mm");	
		String data = dataFormatada.format(agenda.getData());
		String hora = horaFormatada.format(agenda.getHoraDe());
		StringBuilder mensagem = new StringBuilder("<html>");			
		mensagem.append("<body>");
		mensagem.append("<p><b>Olá ").append(cliente.getNome()).append("</b>,</p>");
		mensagem.append("<p>Seguem as informações do seu agendamento no Vasconcelos Studio de Beleza.</p>");			
		mensagem.append("<p>Seu agendamento foi confirmado para o dia ").append(data).append(" as ").append(hora).append(".</p>");
		mensagem.append("<p>Com a profissional ").append(profissional.getNome()).append(".</p>");
		mensagem.append("<br/>");
		mensagem.append("<p><b>Obs: Qualquer imprevisto por favor desmarcar o agendamento com antecedência<b></p>");
		mensagem.append("<p>Curta nossa pagina no Facebook: <a href=\"http://www.facebook.com/vasconcelosstudiodebeleza/\">www.facebook.com/vasconcelosstudiodebeleza</a> </p>");	
		mensagem.append("<br/>");
		mensagem.append("</body>");
		mensagem.append("</html>");		
		EmailServico emailServico = new EmailServico();
		emailServico.enviar("Vasconcelos Studio", cliente.getEmail(), "Agendamento", mensagem.toString());
	}

}