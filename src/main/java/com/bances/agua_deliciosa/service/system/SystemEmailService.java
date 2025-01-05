package com.bances.agua_deliciosa.service.system;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("systemEmailService")
public class SystemEmailService {

    public void sendEmail(String to, String subject, String text) {
        // Simulación de envío de correo para pruebas del sistema
        log.info("SISTEMA - Enviando email a: {} con asunto: {}", to, subject);
        log.info("SISTEMA - Contenido del mensaje: {}", text);
    }

    public void sendWelcomeEmail(String email, String name, String verificationLink) {
        String subject = "¡Bienvenido al Sistema!";
        String text = String.format(
            "SISTEMA - Bienvenido %s,\n" +
            "Se ha creado tu cuenta en el sistema.\n" +
            "Enlace de verificación: %s", 
            name, verificationLink);
        sendEmail(email, subject, text);
    }

    public void sendPasswordChangedEmail(String email) {
        sendEmail(email, 
                 "SISTEMA - Contraseña Actualizada",
                 "Tu contraseña ha sido actualizada en el sistema.");
    }

    public void sendEmailVerifiedNotification(String email) {
        sendEmail(email,
                 "SISTEMA - Email Verificado",
                 "Tu email ha sido verificado en el sistema.");
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        String text = String.format(
            "SISTEMA - Has solicitado restablecer tu contraseña.\n" +
            "Usa este enlace: %s",
            resetLink);
        sendEmail(email, "SISTEMA - Restablecer Contraseña", text);
    }
}