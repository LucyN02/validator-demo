package com.validator.demo.controller;


import com.validator.demo.dto.ValidatorRequestDTO;
import com.validator.demo.dto.ValidatorResponseDTO;
import com.validator.demo.model.interfaces.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/validators")
public class ValidatorController {

    private final Validator validator;

    public ValidatorController(Validator validator) {
        this.validator = validator;
    }

    @PostMapping("/password")
    public ValidatorResponseDTO validatePassword(@Valid @RequestBody ValidatorRequestDTO request) {
        return this.validator.execute(request.value());
    }
}
