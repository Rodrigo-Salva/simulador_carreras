package com.usuarios_tokens.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityEventListener {
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        log.info("Login exitoso: {}", event.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        log.warn("Login fallido: {}", event.getAuthentication().getName());
    }
}
