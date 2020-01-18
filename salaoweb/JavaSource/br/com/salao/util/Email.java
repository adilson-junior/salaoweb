package br.com.salao.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Email {
    static final String REMETENTE_NOME = "adilsonvajr@gmail.com";
    static final String REMETENTE_SENHA = "505240junior";
    private String assunto ;
    private String mensagem;
    private String destinatario;    
    /**
     * 
     * @param assunto  Assunto para envio do E-mail.
     * @param mensagem  Mensagem qual vai ser enviado ao destinat�rio.
     * @param destinatario  E-mail do destinat�rio.
     */
    public Email(String assunto, String mensagem, String destinatario) {
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }
    
    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
   
    /**
     * Fun��o para enviar o e-mail ao destinat�rio.
     */
    public void enviar (){
        SimpleEmail email = new SimpleEmail();          
            email.setSSLOnConnect(true);
            email.setHostName("smtp.gmail.com");
            email.setSslSmtpPort("465");
       email.setAuthenticator(new DefaultAuthenticator(REMETENTE_NOME, REMETENTE_SENHA));
       try {
           email.setFrom(REMETENTE_NOME);

           email.setDebug(true);

           email.setSubject(this.assunto);
           email.setMsg(this.mensagem);
           email.addTo(this.destinatario);//por favor trocar antes de testar!!!!

           email.send();

       } catch (EmailException e) {
           e.printStackTrace();
       }
    }
}
