package com.bances.agua_deliciosa.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendWelcomeEmail(String to, String name, String verificationLink);
    void sendPasswordChangedEmail(String email);
    void sendEmailVerifiedNotification(String email);
    void sendResetPasswordLink(String email, String resetLink);
} 