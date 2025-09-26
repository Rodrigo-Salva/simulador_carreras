package com.usuarios_tokens.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Usuario/email es obligatorio")
    @Size(min = 3, max = 100, message = "Usuario/email debe tener entre 3 y 100 caracteres")
    private String usernameOrEmail;

    @NotBlank(message = "Contraseña es obligatoria")
    @Size(min = 1, max = 255, message = "Contraseña inválida")
    private String password;
}
