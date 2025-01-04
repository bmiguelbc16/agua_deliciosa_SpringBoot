package com.bances.agua_deliciosa.service.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import com.bances.agua_deliciosa.service.EmailService;

@Slf4j
@Service("notificationEmailService")
public class NotificationEmailService implements EmailService {
    
    private final JavaMailSender mailSender;
    private final String fromEmail;
    
    public NotificationEmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }
    
    @Override
    public void sendEmail(String to, String subject, String text) {
        log.info("Enviando email a: {} con asunto: {}", to, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    @Override
    public void sendEmailVerifiedNotification(String to) {
        sendEmail(to, 
                 "¡Email Verificado!", 
                 "Tu email ha sido verificado exitosamente. Ya puedes acceder a todas las funcionalidades de la aplicación.");
    }
    
    @Override
    public void sendResetPasswordLink(String to, String resetLink) {
        sendEmail(to,
                 "Restablecer Contraseña",
                 "Has solicitado restablecer tu contraseña. Por favor, sigue este enlace para crear una nueva contraseña: "
                 + resetLink
                 + "\n\nSi no solicitaste restablecer tu contraseña, puedes ignorar este mensaje.");
    }
    
    @Override
    public void sendWelcomeEmail(String to, String name, String verificationLink) {
        sendEmail(to,
                 "¡Bienvenido a Agua Deliciosa!",
                 "Hola " + name + ",\n\n"
                 + "¡Bienvenido a Agua Deliciosa! Estamos encantados de tenerte con nosotros.\n\n"
                 + "Por favor, verifica tu email haciendo clic en el siguiente enlace:\n"
                 + verificationLink + "\n\n"
                 + "Si tienes alguna pregunta, no dudes en contactarnos.\n\n"
                 + "¡Gracias por confiar en nosotros!\n"
                 + "El equipo de Agua Deliciosa");
    }

    @Override
    public void sendPasswordChangedEmail(String email) {
        sendEmail(
            email,
            "Contraseña Actualizada",
            "Tu contraseña ha sido actualizada exitosamente."
        );
    }
}
