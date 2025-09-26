package com.usuarios_tokens.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;

    private Long usuarioId;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String tipoUsuario;
    private Boolean emailVerificado;

    @Builder.Default
    private Long loginTimestamp = System.currentTimeMillis();
}
