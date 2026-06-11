package com.validator.demo.dto;

import java.util.List;

public record ValidatorResponseDTO(
        boolean valid,
        List<String> errors
) { }
