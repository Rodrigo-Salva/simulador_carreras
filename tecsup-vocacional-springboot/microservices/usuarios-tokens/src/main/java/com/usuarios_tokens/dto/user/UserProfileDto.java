package com.usuarios_tokens.dto.user;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private Long usuarioId;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String tipoUsuario;
    private Boolean emailVerificado;
    private LocalDateTime fechaCreacion;

    // Datos del perfil
    @Size(max = 500, message = "Biografía máximo 500 caracteres")
    private String biografia;
    private String carreraInteres;
    private String nivelEducativo;
    private String ciudad;
    private String region;
    private String linkedinUrl;
    private String githubUrl;
    private String portafolioUrl;
    private Boolean preferenciaNotificacion;
    private String temaInterfaz;
    private String idiomaPreferido;
}
