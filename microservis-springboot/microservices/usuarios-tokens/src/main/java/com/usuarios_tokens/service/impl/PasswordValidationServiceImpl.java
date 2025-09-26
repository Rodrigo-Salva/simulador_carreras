package com.usuarios_tokens.service.impl;


import com.usuarios_tokens.dto.validation.PasswordValidationDto;
import com.usuarios_tokens.dto.validation.ValidationResponseDto;
import com.usuarios_tokens.service.interfaces.IPasswordValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PasswordValidationServiceImpl implements IPasswordValidationService {

    @Value("${app.password.min-length:8}")
    private int minLength;

    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    private static final Pattern NO_SPACES = Pattern.compile("^\\S*$");

    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "123456", "password", "123456789", "12345678", "12345",
            "admin", "tecsup", "student", "qwerty", "letmein",
            "welcome", "monkey", "dragon", "pass", "master"
    );

    @Override
    public ValidationResponseDto validatePassword(PasswordValidationDto dto) {
        log.debug("Validating password for: {}", dto.getNombreUsuario());

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        int strength = 0;
        String password = dto.getPassword();

        strength += validateLength(password, errors);
        strength += validateCharacterTypes(password, errors);
        strength += validateNoSpaces(password, errors);
        strength += validateNotCommon(password, warnings);
        strength += validateNoPersonalInfo(password, dto, warnings);

        strength = Math.max(0, Math.min(100, strength));

        return ValidationResponseDto.builder()
                .isValid(errors.isEmpty())
                .strengthScore(strength)
                .strengthLevel(getStrengthLevel(strength))
                .errors(errors)
                .warnings(warnings)
                .recommendations(generatePasswordRecommendations(password))
                .build();
    }

    @Override
    public List<String> generatePasswordRecommendations(String password) {
        List<String> recommendations = new ArrayList<>();

        int strength = calculatePasswordStrength(password);

        if (strength < 70) {
            recommendations.add("Combina mayúsculas, minúsculas, números y símbolos");
            recommendations.add("Usa al menos 12 caracteres para mayor seguridad");
            recommendations.add("Evita información personal como nombres o fechas");
        }

        recommendations.add("Considera usar un gestor de contraseñas");
        recommendations.add("Cambia tu contraseña periódicamente");

        return recommendations;
    }

    @Override
    public Integer calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) return 0;

        int score = 0;
        if (password.length() >= 12) score += 25;
        else if (password.length() >= 8) score += 15;

        if (UPPERCASE.matcher(password).matches()) score += 20;
        if (LOWERCASE.matcher(password).matches()) score += 20;
        if (DIGIT.matcher(password).matches()) score += 20;
        if (SPECIAL_CHAR.matcher(password).matches()) score += 15;

        if (isCommonPassword(password)) score -= 20;

        return Math.max(0, Math.min(100, score));
    }

    @Override
    public Boolean meetsMinimumRequirements(String password) {
        if (password == null || password.length() < minLength) return false;

        return UPPERCASE.matcher(password).matches() &&
                LOWERCASE.matcher(password).matches() &&
                DIGIT.matcher(password).matches() &&
                SPECIAL_CHAR.matcher(password).matches() &&
                NO_SPACES.matcher(password).matches();
    }

    private int validateLength(String password, List<String> errors) {
        if (password.length() < minLength) {
            errors.add(String.format("Mínimo %d caracteres requeridos", minLength));
            return 0;
        }
        if (password.length() >= 12) return 25;
        return 15;
    }

    private int validateCharacterTypes(String password, List<String> errors) {
        int score = 0;

        if (!UPPERCASE.matcher(password).matches()) {
            errors.add("Al menos una letra MAYÚSCULA (A-Z)");
        } else score += 15;

        if (!LOWERCASE.matcher(password).matches()) {
            errors.add("Al menos una letra minúscula (a-z)");
        } else score += 15;

        if (!DIGIT.matcher(password).matches()) {
            errors.add("Al menos un número (0-9)");
        } else score += 15;

        if (!SPECIAL_CHAR.matcher(password).matches()) {
            errors.add("Al menos un símbolo (!@#$%^&*)");
        } else score += 15;

        return score;
    }

    private int validateNoSpaces(String password, List<String> errors) {
        if (!NO_SPACES.matcher(password).matches()) {
            errors.add("No puede contener espacios");
            return -10;
        }
        return 0;
    }

    private int validateNotCommon(String password, List<String> warnings) {
        if (isCommonPassword(password)) {
            warnings.add("Esta contraseña es muy común");
            return -20;
        }
        return 10;
    }

    private int validateNoPersonalInfo(String password, PasswordValidationDto dto, List<String> warnings) {
        String lowerPassword = password.toLowerCase();

        if (dto.getNombreUsuario() != null &&
                lowerPassword.contains(dto.getNombreUsuario().toLowerCase())) {
            warnings.add("No usar nombre de usuario en contraseña");
            return -15;
        }

        return 5;
    }

    private boolean isCommonPassword(String password) {
        String lowerPassword = password.toLowerCase();
        return COMMON_PASSWORDS.stream()
                .anyMatch(common -> lowerPassword.contains(common));
    }

    private String getStrengthLevel(int score) {
        if (score >= 85) return "MUY_FUERTE";
        if (score >= 70) return "FUERTE";
        if (score >= 50) return "MODERADA";
        if (score >= 30) return "DÉBIL";
        return "MUY_DÉBIL";
    }
}
