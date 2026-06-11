package com.validator.demo.model.interfaces;

import com.validator.demo.dto.ValidatorResponseDTO;

public interface Validator {
    ValidatorResponseDTO execute(String value);
}
