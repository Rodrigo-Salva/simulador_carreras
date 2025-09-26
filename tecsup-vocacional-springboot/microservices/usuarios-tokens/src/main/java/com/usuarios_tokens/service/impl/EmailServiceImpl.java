package com.usuarios_tokens.service.impl;

import com.usuarios_tokens.entity.Usuario;
import com.usuarios_tokens.service.interfaces.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {
    @Override
    public void sendEmailVerification(Usuario usuario, String token) {
        log.info("[EMAIL] Enviando verificación a {} con token {}", usuario.getEmail(), token);
    }

    @Override
    public void sendPasswordResetEmail(Usuario usuario, String token) {
        log.info("[EMAIL] Enviando reset password a {} con token {}", usuario.getEmail(), token);
    }

    @Override
    public void sendWelcomeEmail(Usuario usuario) {
        log.info("[EMAIL] Bienvenida enviada a {}", usuario.getEmail());
    }

    @Override
    public void sendPasswordChangeNotification(Usuario usuario) {
        log.info("[EMAIL] Notificación de cambio de contraseña a {}", usuario.getEmail());
    }

    @Override
    public void sendAccountDeactivationEmail(Usuario usuario) {
        log.info("[EMAIL] Notificación de desactivación a {}", usuario.getEmail());
    }

    @Override
    public String generateVerificationToken(Usuario usuario) {
        return "verif-" + usuario.getUsuarioId() + "-" + System.currentTimeMillis();
    }

    @Override
    public String generatePasswordResetToken(Usuario usuario) {
        return "reset-" + usuario.getUsuarioId() + "-" + System.currentTimeMillis();
    }
}
