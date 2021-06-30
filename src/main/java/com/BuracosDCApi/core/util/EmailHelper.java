package com.BuracosDCApi.core.util;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.BuracosDCApi.core.model.Arquivo;

import java.util.Properties;

@SuppressWarnings("unused")
public class EmailHelper {
	
	// boa sorte pra implementar isso kkk

//	/**
//	 * Credenciais do e-mail usadas para se autenticar no servidor SMTP.
//	 */
//	private static final String EMAIL = "teu@email";
//	private static final String SENHA = ":P";
//
//	/**
//	 * Textos de assuntos do e-mail.
//	 */
//	private static final String ASSUNTO_CADASTRO = "[BURACOS DA CIDADE] - Confirmação de cadastro";
//	private static final String ASSUNTO_RESET_SENHA = "[BURACOS DA CIDADE] - Redefinição de senha";
//	private static final String ASSUNTO_ALERTA_EXPIRACAO_SENHA = "[BURACOS DA CIDADE] - Alerta sobre expiração de senha";
//	private static final String ASSUNTO_EXPIRACAO_SENHA = "[BURACOS DA CIDADE] - Expiração de senha";
//	private static final String ASSUNTO_NOTIFICACAO = "[BURACOS DA CIDADE] - Notificação";
//
//	/**
//	 * Parâmetros de e-mails e servidor SMTP.
//	 */
//	private static final String HOST = "smtp.gmail.com";
//	private static final String SSL_TRUST = "*";
//	private static final String PORT = "587";
//	private static final Boolean TTLS_ENABLE = true;
//	private static final String AUTH = "true";
//
//	/**
//	 * Sessão própria para enviar e-mails.
//	 */
//	private static Session mailSession;
//
//	/**
//	 * Função de enviar e-mail (com o parâmetro de sessão omitido).
//	 * 
//	 * @param from    Remetente
//	 * @param to      Destinatário
//	 * @param assunto Assunto
//	 * @param texto   Texto
//	 */
//	public static void enviarEmail(String from, String to, String assunto, String texto) {
//		enviarEmail(from, to, assunto, texto, getMailSession());
//	}
//
//	/**
//	 * Função de enviar e-mail padrão para novo usuário no sistema (com o parâmetro
//	 * de sessão omitido).
//	 * 
//	 * @param pessoa Destinatário.
//	 */
//	public static void enviarEmailNovoUsuario(Pessoa pessoa, String login, String senha, String serverUrl) {
//		enviarEmailNovoUsuario(pessoa.getEmail(), senha, login, pessoa.getNome(), getMailSession(), serverUrl);
//	}
//
//	/**
//	 * Envia e-mail sobre reset de senha.
//	 * 
//	 * @param pessoa Pessoa que receberá o e-mail
//	 */
//	public static void enviarEmailResetSenha(Pessoa pessoa, String senha, String serverUrl) {
//		enviarEmailResetSenha(pessoa.getEmail(), pessoa.getNome(), senha, getMailSession(), serverUrl);
//	}
//
//	/**
//	 * Envia e-mail sobre aviso de expiração de senha.
//	 * 
//	 * @param pessoa Pessoa que receberá o e-mail
//	 */
//	public static void enviarEmailAvisoExpiracaoSenha(Pessoa pessoa, String serverUrl) {
//		enviarEmailAvisoExpiracaoSenha(pessoa.getEmail(), pessoa.getNome(), getMailSession(), serverUrl);
//	}
//
//	/**
//	 * Envia e-mail sobre expiração de senha.
//	 * 
//	 * @param pessoa Pessoa que receberá o e-mail
//	 */
//	public static void enviarEmailExpiracaoSenha(Pessoa pessoa, String serverUrl) {
//		enviarEmailExpiracaoSenha(pessoa.getEmail(), pessoa.getNome(), getMailSession(), serverUrl);
//	}
//
//	/**
//	 * Envia e-mail de notificação do sistema ao usuário.
//	 * 
//	 * @param pessoa      Pessoa que receberá o e-mail
//	 */
//	public static void enviarEmailNotificacao(Pessoa pessoa, String titulo, String conteudo, String serverUrl) {
//		enviarEmailNotificacao(pessoa.getEmail(), pessoa.getNome(), titulo, conteudo, getMailSession(), serverUrl);
//	}
//
//	/**
//	 * Função de enviar e-mail com anexo (com o parâmetro de sessão omitido).
//	 * 
//	 * @param from      Remetente
//	 * @param to        Destinatário
//	 * @param assunto   Assunto
//	 * @param texto     Texto
//	 * @param tipoTexto Tipo de texto do e-mail
//	 * @param attach    Arquivo a ser anexado
//	 */
//	public static void enviarEmailComAnexo(String from, String to, String assunto, String texto, String tipoTexto,
//			Arquivo attach) {
//		enviarEmailComAnexo(from, to, assunto, texto, tipoTexto, attach, getMailSession());
//	}
//
//	/**
//	 * Método que efetua o envio de e-mail de acordo com os parametros recebidos
//	 * 
//	 * @param from, to, assunto, texto
//	 */
//	private static void enviarEmail(String from, String to, String assunto, String texto, Session mailSession) {
//		EnviarEmailThread email = new EnviarEmailThread(from, to, assunto, texto, mailSession);
//		email.start();
//	}
//
//	/**
//	 * Efetua o envio de e-mail para um novo usuário.
//	 */
//	private static void enviarEmailNovoUsuario(String to, String senha, String login, String nomeUsuario, Session mailsSession, String serverUrl) {
//		String template = getMailStringFromTemplate(login, senha, null, null, nomeUsuario, "email_confirmacao", serverUrl);
//		EnviarEmailThread email = new EnviarEmailThread(EMAIL, to, ASSUNTO_CADASTRO, template, "text/html",
//				mailsSession);
//		email.start();
//	}
//
//	/**
//	 * Efetua o envio de e-mail para um novo usuário.
//	 */
//	private static void enviarEmailResetSenha(String to, String nomeUsuario, String senha, Session mailsSession, String serverUrl) {
//		String template = getMailStringFromTemplate(null, senha, null, null, nomeUsuario, "email_reset_senha", serverUrl);
//		EnviarEmailThread email = new EnviarEmailThread(EMAIL, to, ASSUNTO_RESET_SENHA, template, "text/html",
//				mailsSession);
//		email.start();
//	}
//
//	private static void enviarEmailAvisoExpiracaoSenha(String to, String nomeUsuario, Session mailSession, String serverUrl) {
//		String template = getSimpleMailStringFromTemplate(nomeUsuario, "email_aviso_expiracao_senha", serverUrl);
//		EnviarEmailThread email = new EnviarEmailThread(EMAIL, to, ASSUNTO_ALERTA_EXPIRACAO_SENHA, template,
//				"text/html", mailSession);
//		email.start();
//	}
//
//	private static void enviarEmailExpiracaoSenha(String to, String nomeUsuario, Session mailSession, String serverUrl) {
//		String template = getSimpleMailStringFromTemplate(nomeUsuario, "email_expiracao_senha", serverUrl);
//		EnviarEmailThread email = new EnviarEmailThread(EMAIL, to, ASSUNTO_EXPIRACAO_SENHA, template, "text/html",
//				mailSession);
//		email.start();
//	}
//
//	private static void enviarEmailNotificacao(String to, String nomeUsuario, String titulo, String conteudo,
//			Session mailSession, String serverUrl) {
//		String template = getNotificacaoMailStringFromTemplate(titulo, conteudo, nomeUsuario, serverUrl);
//		EnviarEmailThread email = new EnviarEmailThread(EMAIL, to, ASSUNTO_NOTIFICACAO, template, "text/html",
//				mailSession);
//		email.start();
//	}
//
//	/**
//	 * Envia e-mail com anexo.
//	 */
//	private static void enviarEmailComAnexo(String from, String to, String assunto, String texto, String tipoTexto,
//			Arquivo attach, Session session) {
//		EnviarEmailThread email = new EnviarEmailThread(from, to, assunto, texto, tipoTexto, attach, session);
//		email.start();
//	}
//
//	private static String getNotificacaoMailStringFromTemplate(String titulo, String notificacao, String nomeUsuario, String serverUrl) {
//		return getMailStringFromTemplate(null, null, titulo, notificacao, nomeUsuario, "email_notificacao", serverUrl);
//	}
//
//	private static String getSimpleMailStringFromTemplate(String nomeUsuario, String template, String serverUrl) {
//		return getMailStringFromTemplate(null, null, null, null, nomeUsuario, template, serverUrl);
//	}
//
//	/**
//	 * Gera um texto HTML, correspondente ao template de e-mail, com as informações
//	 * do usuário.
//	 * 
//	 * @param login       Login do usuário
//	 * @param senha       Senha do usuário
//	 * @param titulo      Título da notificação que gerou o e-mail
//	 * @param conteudo    Conteúdo da notificação que gerou o e-mail
//	 * @param nomeUsuario Nome do usuário
//	 * @param template    Nome do template Thymeleaf
//	 * @return String correspondente ao e-mail.
//	 */
//	private static String getMailStringFromTemplate(String login, String senha, String titulo, String conteudo,
//			String nomeUsuario, String template, String serverUrl) {
//		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//		templateResolver.setPrefix("templates/");
//		templateResolver.setSuffix(".html");
//		templateResolver.setTemplateMode("HTML");
//		templateResolver.setCharacterEncoding("UTF-8");
//		templateResolver.setOrder(1);
//		TemplateEngine templateEngine = new TemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver);
//		Context context = new Context();
//		context.setVariable("login", login);
//		context.setVariable("senha", senha);
//		context.setVariable("nomeUsuario", nomeUsuario);
//		context.setVariable("titulo", titulo);
//		context.setVariable("conteudo", conteudo);
//		context.setVariable("url", serverUrl);
//		String html = "";
//		html = templateEngine.process(template, context);
//		return html;
//	}
//
//	/**
//	 * Recupera a sessão de e-mail.
//	 */
//	public static Session getMailSession() {
//		if (mailSession == null) {
//			Properties props = new Properties();
//			props.put("mail.smtp.host", HOST);
//			props.put("mail.smtp.ssl.trust", SSL_TRUST);
//			props.put("mail.smtp.socketFactory.port", PORT);
//			props.put("mail.smtp.starttls.enable", TTLS_ENABLE);
//			props.put("mail.smtp.auth", AUTH);
//			props.put("mail.smtp.port", PORT);
//
//			mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(EMAIL, SENHA);
//				}
//			});
//		}
//		return mailSession;
//	}

}