package com.validator.demo.dto;

import jakarta.validation.constraints.NotBlank;


public record ValidatorRequestDTO(
        @NotBlank
        String value
) { }
