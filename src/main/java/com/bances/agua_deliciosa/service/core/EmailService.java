package com.bances.agua_deliciosa.service.core;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender, 
                       @Value("${spring.mail.username:noreply@aguadeliciosa.com}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            log.info("Enviando email a: {} con asunto: {}", to, subject);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email enviado exitosamente a: {}", to);
        } catch (Exception e) {
            log.error("Error al enviar email a: {}", to, e);
            throw new RuntimeException("Error al enviar email", e);
        }
    }

    public void sendWelcomeEmail(String email, String name, String verificationLink) {
        String subject = "¡Bienvenido a Agua Deliciosa!";
        String text = String.format("""
            Hola %s,
            
            ¡Bienvenido a Agua Deliciosa! Estamos encantados de tenerte con nosotros.
            
            Para verificar tu cuenta, por favor haz clic en el siguiente enlace:
            %s
            
            Si no creaste esta cuenta, puedes ignorar este mensaje.
            
            Saludos,
            El equipo de Agua Deliciosa
            """, name, verificationLink);
        
        sendEmail(email, subject, text);
    }

    public void sendPasswordResetEmail(String email, String token) {
        String subject = "Restablecer contraseña - Agua Deliciosa";
        String text = String.format("""
            Hola,
            
            Has solicitado restablecer tu contraseña en Agua Deliciosa.
            
            Para restablecer tu contraseña, utiliza el siguiente token:
            %s
            
            Este token expirará en 1 hora.
            
            Si no solicitaste restablecer tu contraseña, puedes ignorar este mensaje.
            
            Saludos,
            El equipo de Agua Deliciosa
            """, token);
            
        sendEmail(email, subject, text);
    }

    public void sendPasswordChangedEmail(String email) {
        String subject = "Contraseña actualizada - Agua Deliciosa";
        String text = """
            Hola,
            
            Tu contraseña ha sido actualizada exitosamente.
            
            Si no realizaste este cambio, por favor contáctanos inmediatamente.
            
            Saludos,
            El equipo de Agua Deliciosa
            """;
        
        sendEmail(email, subject, text);
    }

    public void sendEmailVerifiedNotification(String email) {
        String subject = "¡Email Verificado!";
        String text = """
            Hola,
            
            Tu dirección de correo electrónico ha sido verificada exitosamente.
            Ya puedes disfrutar de todos los beneficios de tu cuenta.
            
            Saludos,
            El equipo de Agua Deliciosa
            """;
        
        sendEmail(email, subject, text);
    }
}
