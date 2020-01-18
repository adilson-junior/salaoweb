package br.com.salao.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailServico implements Serializable{

	private static final long serialVersionUID = 1L;	
	private Properties props;
	
	public EmailServico() {
		props = new Properties();	
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host","facilitysoft.com.br");
		props.put("mail.smtp.socketFactory.port","465");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable","false");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.port","465");
		/*props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.auth", "true");*/
	}
	
	public void enviar(String nomeDe, String emailPara, String assunto, String mensagem) throws Exception{		
        try {
        	Session session = Session.getDefaultInstance(props,
    				new javax.mail.Authenticator() {
    					protected PasswordAuthentication getPasswordAuthentication() {
    						return new PasswordAuthentication("vasconcelosestudiodebeleza@facilitysoft.com.br", "505240Jr!@#");
    					}
    				});
    		Message message = new MimeMessage(session);
    		message.setContent(mensagem,"text/html");
    		message.setSubject(assunto);
    		// De
			message.setFrom(new InternetAddress("vasconcelosestudiodebeleza@facilitysoft.com.br", "Vasconcelos Studio de Beleza")); 
			// Para
			Address addressTO = new InternetAddress(emailPara);
			message.setRecipient(Message.RecipientType.TO, addressTO);	   
			message.setSentDate(new Date());
			Transport.send(message);			
		} catch (Exception e) {		
			throw new Exception("Ocorreu um erro ao tentar enviar o e-mail. Verfique se o e-mail existe.");
		} 
	}	
}
