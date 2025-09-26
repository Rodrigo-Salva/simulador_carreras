package com.usuarios_tokens.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Nombre de usuario obligatorio")
    @Size(min = 3, max = 50, message = "Nombre de usuario: 3-50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Solo letras, números y _")
    private String nombreUsuario;

    @NotBlank(message = "Email obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Contraseña obligatoria")
    @Size(min = 8, max = 128, message = "Contraseña: 8-128 caracteres")
    private String password;

    @NotBlank(message = "Confirmar contraseña")
    private String confirmPassword;

    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 2, max = 50, message = "Nombre: 2-50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo letras y espacios")
    private String nombre;

    @NotBlank(message = "Apellido obligatorio")
    @Size(min = 2, max = 50, message = "Apellido: 2-50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Solo letras y espacios")
    private String apellido;

    @Pattern(regexp = "^[0-9]{9}$", message = "Teléfono: 9 dígitos")
    private String telefono;

    @Past(message = "Fecha de nacimiento debe ser pasada")
    private LocalDate fechaNacimiento;
}
