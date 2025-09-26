package com.usuarios_tokens.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;

public interface IJwtService {
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
}
