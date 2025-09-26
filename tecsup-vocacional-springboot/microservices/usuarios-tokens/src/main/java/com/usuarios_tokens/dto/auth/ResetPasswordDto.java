package com.usuarios_tokens.dto.auth;

        import jakarta.validation.constraints.Email;
        import jakarta.validation.constraints.NotBlank;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    @NotBlank(message = "Email obligatorio")
    @Email(message = "Email inválido")
    private String email;
}
