package com.bances.agua_deliciosa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public void sendPasswordResetEmail(String to, String token) {
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado. Email no enviado a: {}", to);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Restablecer Contraseña");
        message.setText("Tu token de restablecimiento es: " + token);
        
        try {
            mailSender.send(message);
            log.info("Email de restablecimiento enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }
    }
    
    public void sendVerificationEmail(String to, Long userId, String hash) {
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado. Email no enviado a: {}", to);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verifica tu Email");
        message.setText("Verifica tu cuenta usando este enlace: /email/verify/" + userId + "/" + hash);
        
        try {
            mailSender.send(message);
            log.info("Email de verificación enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }
    }
}