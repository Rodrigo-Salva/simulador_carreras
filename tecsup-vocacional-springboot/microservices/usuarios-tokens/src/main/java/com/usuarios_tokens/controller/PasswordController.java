package com.usuarios_tokens.controller;

import com.usuarios_tokens.dto.ApiResponseDto;
import com.usuarios_tokens.dto.auth.ResetPasswordDto;
import com.usuarios_tokens.dto.auth.ConfirmResetPasswordDto;
import com.usuarios_tokens.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8000", "http://localhost:4200"})
public class PasswordController {
    private final IUserService userService;

    // Solicitar email de recuperación
    @PostMapping("/forgot")
    public ResponseEntity<ApiResponseDto<String>> forgotPassword(@Valid @RequestBody ResetPasswordDto dto) {
        userService.resendEmailVerificationByEmail(dto.getEmail());
        return ResponseEntity.ok(ApiResponseDto.success("Si el email existe, se envió el enlace de recuperación", null));
    }

    // Confirmar cambio de contraseña con token
    @PostMapping("/reset")
    public ResponseEntity<ApiResponseDto<String>> resetPassword(@Valid @RequestBody ConfirmResetPasswordDto dto) {
        userService.resetPassword(dto.getToken(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.ok(ApiResponseDto.success("Contraseña restablecida correctamente", null));
    }

    // Cambiar contraseña autenticado (opcional, requiere usuario autenticado)
    @PostMapping("/change")
    public ResponseEntity<ApiResponseDto<String>> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("Contraseña cambiada correctamente", null));
    }
}
