package com.usuarios_tokens.dto.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponseDto {

    private Boolean isValid;
    private Integer strengthScore;
    private String strengthLevel;
    private List<String> errors;
    private List<String> warnings;
    private List<String> recommendations;
}
