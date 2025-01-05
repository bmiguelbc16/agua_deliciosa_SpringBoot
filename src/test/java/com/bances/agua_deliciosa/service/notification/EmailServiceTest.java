package com.bances.agua_deliciosa.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.bances.agua_deliciosa.service.core.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;
    private final String fromEmail = "test@aguadeliciosa.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(mailSender, fromEmail);
    }

    @Test
    void sendEmailVerifiedNotification_ShouldSendCorrectEmail() {
        String toEmail = "user@example.com";
        
        emailService.sendEmailVerifiedNotification(toEmail);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage, "El mensaje no debe ser nulo");
        assertEquals(fromEmail, sentMessage.getFrom(), "El remitente debe ser correcto");
        
        String[] toAddresses = sentMessage.getTo();
        assertNotNull(toAddresses, "El destinatario no debe ser nulo");
        assertTrue(toAddresses.length > 0, "Debe haber al menos un destinatario");
        assertEquals(toEmail, toAddresses[0]);
        
        assertEquals("¡Email Verificado!", sentMessage.getSubject());
        
        String messageText = sentMessage.getText();
        assertNotNull(messageText, "El texto del mensaje no debe ser nulo");
        assertTrue(messageText.contains("verificada exitosamente"), 
                  "El mensaje debe contener la confirmación de verificación");
    }

    @Test
    void sendWelcomeEmail_ShouldSendCorrectEmail() {
        String toEmail = "user@example.com";
        String name = "John Doe";
        String verificationLink = "http://example.com/verify";
        
        emailService.sendWelcomeEmail(toEmail, name, verificationLink);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage, "El mensaje no debe ser nulo");
        assertEquals(fromEmail, sentMessage.getFrom());
        
        String[] toAddresses = sentMessage.getTo();
        assertNotNull(toAddresses, "El destinatario no debe ser nulo");
        assertTrue(toAddresses.length > 0, "Debe haber al menos un destinatario");
        assertEquals(toEmail, toAddresses[0]);
        
        assertEquals("¡Bienvenido a Agua Deliciosa!", sentMessage.getSubject());
        
        String messageText = sentMessage.getText();
        assertNotNull(messageText, "El texto del mensaje no debe ser nulo");
        assertTrue(messageText.contains(name), "El mensaje debe contener el nombre");
        assertTrue(messageText.contains(verificationLink), "El mensaje debe contener el enlace");
    }

    @Test
    void sendPasswordResetEmail_ShouldSendCorrectEmail() {
        String toEmail = "user@example.com";
        String resetLink = "http://example.com/reset";
        
        emailService.sendPasswordResetEmail(toEmail, resetLink);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage, "El mensaje no debe ser nulo");
        assertEquals(fromEmail, sentMessage.getFrom());
        
        String[] toAddresses = sentMessage.getTo();
        assertNotNull(toAddresses, "El destinatario no debe ser nulo");
        assertTrue(toAddresses.length > 0, "Debe haber al menos un destinatario");
        assertEquals(toEmail, toAddresses[0]);
        
        assertEquals("Restablecer contraseña - Agua Deliciosa", sentMessage.getSubject());
        
        String messageText = sentMessage.getText();
        assertNotNull(messageText, "El texto del mensaje no debe ser nulo");
        assertTrue(messageText.contains(resetLink), "El mensaje debe contener el enlace");
    }
}
