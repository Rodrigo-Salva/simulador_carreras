package com.usuarios_tokens.service.impl;

import com.usuarios_tokens.dto.auth.AuthResponseDto;
import com.usuarios_tokens.dto.auth.LoginRequestDto;
import com.usuarios_tokens.dto.auth.RegisterRequestDto;
import com.usuarios_tokens.entity.Usuario;
import com.usuarios_tokens.repository.UsuarioRepository;
import com.usuarios_tokens.service.interfaces.IAuthService;
import com.usuarios_tokens.service.interfaces.IJwtService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) throws AuthException {
        log.info("Registering user: {}", request.getNombreUsuario());

        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new AuthException("Username already exists");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already registered");
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(request.getNombreUsuario())
                .email(request.getEmail())
                .passwordEncriptado(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .fechaNacimiento(request.getFechaNacimiento())
                .build();

        usuario = usuarioRepository.save(usuario);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", usuario.getUsuarioId());
        extraClaims.put("userType", usuario.getTipoUsuario().name());

        String accessToken = jwtService.generateToken(extraClaims, usuario);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken("refresh_" + accessToken.substring(accessToken.length() - 20))
                .expiresIn(86400L)
                .usuarioId(usuario.getUsuarioId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .tipoUsuario(usuario.getTipoUsuario().name())
                .emailVerificado(usuario.getEmailVerificado())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto authenticate(LoginRequestDto request) throws AuthException {
        log.info("Authenticating user: {}", request.getUsernameOrEmail());

        Usuario usuario = usuarioRepository.findByNombreUsuarioOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getNombreUsuario(),
                        request.getPassword()
                )
        );

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", usuario.getUsuarioId());
        extraClaims.put("userType", usuario.getTipoUsuario().name());

        String accessToken = jwtService.generateToken(extraClaims, usuario);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken("refresh_" + accessToken.substring(accessToken.length() - 20))
                .expiresIn(86400L)
                .usuarioId(usuario.getUsuarioId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .tipoUsuario(usuario.getTipoUsuario().name())
                .emailVerificado(usuario.getEmailVerificado())
                .build();
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Implementar mañana");
    }

    @Override
    public void logout(String token) {
        log.info("Logout requested");
    }
}
