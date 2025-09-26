package com.usuarios_tokens.service.interfaces;


import com.usuarios_tokens.dto.validation.PasswordValidationDto;
import com.usuarios_tokens.dto.validation.ValidationResponseDto;

import java.util.List;

public interface IPasswordValidationService {
    ValidationResponseDto validatePassword(PasswordValidationDto passwordDto);
    List<String> generatePasswordRecommendations(String password);
    Integer calculatePasswordStrength(String password);
    Boolean meetsMinimumRequirements(String password);
}
