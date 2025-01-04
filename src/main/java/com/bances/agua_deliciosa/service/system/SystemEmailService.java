package com.bances.agua_deliciosa.service.system;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.bances.agua_deliciosa.service.EmailService;

@Slf4j
@Service("systemEmailService")
public class SystemEmailService implements EmailService {
    
    @Override
    public void sendEmail(String to, String subject, String text) {
        // Solo registrar el intento de envío
        log.info("Simulando envío de email a: {} con asunto: {}", to, subject);
        log.debug("Contenido del email: {}", text);
    }
    
    @Override
    public void sendWelcomeEmail(String to, String name, String verificationLink) {
        sendEmail(
            to,
            "Bienvenido a Agua Deliciosa",
            "Hola " + name + ",\n" +
            "Gracias por registrarte en nuestro sistema.\n" +
            "Por favor verifica tu email en: " + verificationLink
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
    
    @Override
    public void sendResetPasswordLink(String email, String token) {
        sendEmail(
            email,
            "Restablecer Contraseña",
            "Para restablecer tu contraseña, usa este token: " + token
        );
    }
}