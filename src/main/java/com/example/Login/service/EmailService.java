package com.example.Login.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

//	@Autowired
//	private JavaMailSender mailSender;
	/*
	 * public void enviarCorreo(String para, String asunto, String cuerpoHtml) { try
	 * { MimeMessage mensaje = mailSender.createMimeMessage(); MimeMessageHelper
	 * helper = new MimeMessageHelper(mensaje, true, "UTF-8");
	 * 
	 * helper.setFrom("grupo1sem12025@gmail.com"); // Asegurate de que esté
	 * verificado en SendGrid helper.setTo(para); helper.setSubject(asunto);
	 * helper.setText(cuerpoHtml, true); // 👈 true indica HTML
	 * 
	 * mailSender.send(mensaje); } catch (MessagingException e) {
	 * e.printStackTrace(); // o loguealo mejor } } // public void
	 * enviarCorreo(String para, String asunto, String cuerpoHtml) { //
	 * SimpleMailMessage mensaje = new SimpleMailMessage(); // mensaje.setTo(para);
	 * // mensaje.setSubject(asunto); // mensaje.setText(cuerpoHtml); //
	 * mensaje.setFrom("grupo1sem12025@gmail.com"); // Opcional: debe estar
	 * verificado si usás un dominio propio // // mailSender.send(mensaje); // // }
	 * ///
	 */

	public void enviarCorreo(String para, String asunto, String cuerpoHtml) {// public static void main(String[] args)																		// throws IOException {
		Email from = new Email("grupo1sem12025@gmail.com");
		// String subject = "Hola desde SendGrid";
		String subject = asunto;// "Hola desde SendGrid";
		Email to = new Email(para);// "destinatario@ejemplo.com");
		// Content content = new Content("text/plain", "Este es el contenido del
		// email");
		Content content = new Content("text/html", cuerpoHtml);
		Mail mail = new Mail(from, subject, to, content);
		SendGrid sg = new SendGrid("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");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			// throw ex;
		}
	}

}