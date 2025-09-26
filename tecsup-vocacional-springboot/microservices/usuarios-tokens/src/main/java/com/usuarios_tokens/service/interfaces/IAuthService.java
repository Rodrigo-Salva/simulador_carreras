package com.usuarios_tokens.service.interfaces;

import com.usuarios_tokens.dto.auth.AuthResponseDto;
import com.usuarios_tokens.dto.auth.LoginRequestDto;
import com.usuarios_tokens.dto.auth.RegisterRequestDto;
import jakarta.security.auth.message.AuthException;

public interface IAuthService {
    AuthResponseDto register(RegisterRequestDto request) throws AuthException;
    AuthResponseDto authenticate(LoginRequestDto request) throws AuthException;
    AuthResponseDto refreshToken(String refreshToken);
    void logout(String token);
}
