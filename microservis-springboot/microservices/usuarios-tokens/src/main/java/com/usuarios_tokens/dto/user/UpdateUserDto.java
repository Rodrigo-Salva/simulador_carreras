package com.usuarios_tokens.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @Size(min = 2, max = 50, message = "Nombre: 2-50 caracteres")
    private String nombre;

    @Size(min = 2, max = 50, message = "Apellido: 2-50 caracteres")
    private String apellido;

    @Pattern(regexp = "^[0-9]{9}$", message = "Teléfono: 9 dígitos")
    private String telefono;

    private LocalDate fechaNacimiento;

    // Campos del perfil
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
