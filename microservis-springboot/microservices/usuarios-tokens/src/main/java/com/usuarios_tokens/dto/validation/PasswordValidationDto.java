package com.usuarios_tokens.dto.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordValidationDto {

    @NotBlank(message = "Contraseña requerida")
    @Size(max = 255, message = "Contraseña demasiado larga")
    private String password;

    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String email;

    public String getUsuarioInfo() {
        StringBuilder info = new StringBuilder();
        if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
            info.append(nombreUsuario.trim()).append(" ");
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            info.append(nombre.trim()).append(" ");
        }
        if (apellido != null && !apellido.trim().isEmpty()) {
            info.append(apellido.trim()).append(" ");
        }
        return info.toString().trim();
    }
}
