package com.BuracosDCApi.core.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.BuracosDCApi.core.model.Arquivo;

/**
 * Thread para envio de email.
 * 
 * @author Lucio Soares de Oliveira.
 */
public class EnviarEmailThread extends Thread {
	private String from;
	private String to;
	private String assunto;
	private String texto;
	private String tipoTexto;
	private Arquivo attach;
	private Session session;

	public EnviarEmailThread(String from, String to, String assunto, String texto, Session session) {
		this.from = from;
		this.to = to;
		this.assunto = assunto;
		this.texto = texto;
		this.session = session;
	}

	public EnviarEmailThread(String from, String to, String assunto, String texto, String tipoTexto, Session session) {
		this.from = from;
		this.to = to;
		this.assunto = assunto;
		this.texto = texto;
		this.tipoTexto = tipoTexto;
		this.session = session;
	}

	public EnviarEmailThread(String from, String to, String assunto, String texto, String tipoTexto, Arquivo attach,
			Session session) {
		this.from = from;
		this.to = to;
		this.assunto = assunto;
		this.texto = texto;
		this.tipoTexto = tipoTexto;
		this.attach = attach;
		this.session = session;

	}

	@Override
	public void run() {
		Message message = new MimeMessage(session);
		// Envia email email com anexo
		if (attach != null) {
			MimeBodyPart mbpTexto = new MimeBodyPart();
			MimeBodyPart mbpAnexo = new MimeBodyPart();
			try {
				message.setSubject(assunto);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.saveChanges();
				mbpTexto.setText(texto);
				DataSource fds = new FileDataSource(attach.getPath());
				mbpAnexo.setDisposition(Part.ATTACHMENT);
				mbpAnexo.setDataHandler(new DataHandler(fds));
				mbpAnexo.setFileName(attach.getNomeOriginal());
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(mbpTexto);
				multipart.addBodyPart(mbpAnexo);
				message.setContent(multipart);
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.setSubject(assunto);
				if (tipoTexto == null) {
					message.setContent(texto, "text/plain; charset=UTF-8");
				} else {
					message.setContent(texto, tipoTexto + "; charset=UTF-8");
				}
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

}