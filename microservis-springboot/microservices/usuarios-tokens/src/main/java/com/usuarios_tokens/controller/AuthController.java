package com.usuarios_tokens.controller;

import com.usuarios_tokens.dto.ApiResponseDto;
import com.usuarios_tokens.dto.auth.*;
import com.usuarios_tokens.dto.validation.PasswordValidationDto;
import com.usuarios_tokens.dto.validation.ValidationResponseDto;
import com.usuarios_tokens.service.interfaces.IAuthService;
import com.usuarios_tokens.service.interfaces.IPasswordValidationService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8000", "http://localhost:4200"})
public class AuthController {

    private final IAuthService authService;
    private final IPasswordValidationService passwordValidationService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponseDto<String>> health() {
        return ResponseEntity.ok(ApiResponseDto.<String>builder()
                .success(true)
                .message("Usuarios-Tokens Service operational")
                .data("v1.0.0 - Sprint 1: Auth & Validation")
                .build());
    }

    @PostMapping("/validate-password")
    public ResponseEntity<ApiResponseDto<ValidationResponseDto>> validatePassword(
            @Valid @RequestBody PasswordValidationDto passwordDto) {

        log.info("Password validation requested");
        ValidationResponseDto validation = passwordValidationService.validatePassword(passwordDto);

        return ResponseEntity.ok(ApiResponseDto.<ValidationResponseDto>builder()
                .success(validation.getIsValid())
                .message(validation.getIsValid()
                        ? "Password valid - " + validation.getStrengthLevel()
                        : "Password does not meet requirements")
                .data(validation)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto registerDto) throws AuthException {

        log.info("Registering user: {}", registerDto.getNombreUsuario());

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<AuthResponseDto>builder()
                    .success(false)
                    .message("Passwords do not match")
                    .build());
        }

        AuthResponseDto response = authService.register(registerDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.<AuthResponseDto>builder()
                        .success(true)
                        .message("Welcome to TECSUP Vocacional!")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginDto) throws AuthException {

        log.info("Login attempt: {}", loginDto.getUsernameOrEmail());

        AuthResponseDto response = authService.authenticate(loginDto);

        return ResponseEntity.ok(ApiResponseDto.<AuthResponseDto>builder()
                .success(true)
                .message("Login successful!")
                .data(response)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        return ResponseEntity.ok(ApiResponseDto.<String>builder()
                .success(true)
                .message("Logout successful")
                .data("Session closed")
                .build());
    }
}
