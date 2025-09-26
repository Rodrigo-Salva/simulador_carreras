package com.usuarios_tokens.dto.auth;

        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.Size;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResetPasswordDto {
    @NotBlank(message = "Token obligatorio")
    private String token;

    @NotBlank(message = "Nueva contraseña obligatoria")
    @Size(min = 8, max = 128, message = "Contraseña: 8-128 caracteres")
    private String newPassword;

    @NotBlank(message = "Confirmar contraseña")
    private String confirmPassword;
}
