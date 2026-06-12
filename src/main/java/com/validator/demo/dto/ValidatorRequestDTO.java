package com.validator.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ValidatorRequestDTO(
        @NotBlank
        @NotNull
        String value
) { }
