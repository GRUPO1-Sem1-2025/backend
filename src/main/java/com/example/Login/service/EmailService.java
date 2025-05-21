package com.example.Login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String para, String asunto, String cuerpoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom("grupo1sem12025@gmail.com"); // Asegurate de que esté verificado en SendGrid
            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true); // 👈 true indica HTML

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace(); // o loguealo mejor
        }
    }
//    public void enviarCorreo(String para, String asunto, String cuerpoHtml) {
//    	 SimpleMailMessage mensaje = new SimpleMailMessage();
//         mensaje.setTo(para);
//         mensaje.setSubject(asunto);
//         mensaje.setText(cuerpoHtml);
//         mensaje.setFrom("grupo1sem12025@gmail.com"); // Opcional: debe estar verificado si usás un dominio propio
//         
//         mailSender.send(mensaje);
//    	
//    }
    
}