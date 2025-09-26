package com.usuarios_tokens.controller;

import com.usuarios_tokens.dto.ApiResponseDto;
import com.usuarios_tokens.dto.user.UpdateUserDto;
import com.usuarios_tokens.dto.user.UserProfileDto;
import com.usuarios_tokens.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8000", "http://localhost:4200"})
public class UserController {
    private final IUserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> getProfile(@PathVariable Long id) {
        UserProfileDto profile = userService.getUserProfile(id);
        return ResponseEntity.ok(ApiResponseDto.success("Perfil obtenido", profile));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        UserProfileDto updated = userService.updateUserProfile(id, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Perfil actualizado", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponseDto.success("Usuario desactivado", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<java.util.List<UserProfileDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponseDto.success("Usuarios obtenidos", userService.getAllUsers()));
    }
}
