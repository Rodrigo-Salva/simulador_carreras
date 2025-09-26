package com.usuarios_tokens.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {

    @NotBlank(message = "Refresh token es obligatorio")
    private String refreshToken;
}
