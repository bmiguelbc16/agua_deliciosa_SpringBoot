package com.bances.agua_deliciosa.service.system;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final String fromEmail;
    
    @Autowired
    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }
    
    private void sendEmail(String to, String subject, String text) {
        log.info("Enviando email a: {} con asunto: {}", to, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    public void sendEmailVerifiedNotification(String to) {
        sendEmail(to, 
                 "¡Email Verificado!", 
                 "Tu email ha sido verificado exitosamente. Ya puedes acceder a todas las funcionalidades de la aplicación.");
    }
    
    public void sendResetPasswordLink(String to) {
        sendEmail(to,
                 "Restablecer Contraseña",
                 "Has solicitado restablecer tu contraseña. Por favor, sigue este enlace para crear una nueva contraseña: "
                 + "\n\nSi no solicitaste restablecer tu contraseña, puedes ignorar este mensaje.");
    }
    
    public void sendWelcomeEmail(String to, String name) {
        sendEmail(to,
                 "¡Bienvenido a Agua Deliciosa!",
                 "Hola " + name + ",\n\n"
                 + "¡Bienvenido a Agua Deliciosa! Estamos encantados de tenerte con nosotros.\n\n"
                 + "Por favor, verifica tu email haciendo clic en el siguiente enlace:\n"
                 + "\n\nSaludos,\nEl equipo de Agua Deliciosa");
    }
    
    public void sendVerificationEmail(String to, String token) {
        sendEmail(to,
                 "Verifica tu Email",
                 "Por favor, verifica tu email haciendo clic en el siguiente enlace:\n"
                 + "\n\nToken de verificación: " + token);
    }
    
    public void sendPasswordResetToken(String to, String token) {
        sendEmail(to,
                 "Token para Restablecer Contraseña",
                 "Has solicitado restablecer tu contraseña.\n"
                 + "Para restablecer tu contraseña, usa este token: " + token);
    }
}