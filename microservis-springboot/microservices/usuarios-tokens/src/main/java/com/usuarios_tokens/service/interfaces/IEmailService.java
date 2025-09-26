package com.usuarios_tokens.service.interfaces;


import com.usuarios_tokens.entity.Usuario;

public interface IEmailService {

    void sendEmailVerification(Usuario usuario, String token);

    void sendPasswordResetEmail(Usuario usuario, String token);

    void sendWelcomeEmail(Usuario usuario);

    void sendPasswordChangeNotification(Usuario usuario);

    void sendAccountDeactivationEmail(Usuario usuario);

    String generateVerificationToken(Usuario usuario);

    String generatePasswordResetToken(Usuario usuario);
}
