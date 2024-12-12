package com.bances.agua_deliciosa.service.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email", e);
        }
    }
    
    public void sendWelcomeEmail(String email) {
        sendEmail(
            email,
            "Bienvenido a Agua Deliciosa",
            "Gracias por registrarte en nuestro sistema."
        );
    }
    
    public void sendPasswordChangedEmail(String email) {
        sendEmail(
            email,
            "Contraseña Actualizada",
            "Tu contraseña ha sido actualizada exitosamente."
        );
    }
    
    public void sendEmailVerifiedNotification(String email) {
        sendEmail(
            email,
            "Email Verificado",
            "Tu correo electrónico ha sido verificado exitosamente."
        );
    }
    
    public void sendPasswordResetEmail(String email, String token) {
        sendEmail(
            email,
            "Restablecer Contraseña",
            "Para restablecer tu contraseña, usa este token: " + token
        );
    }
}