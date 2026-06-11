package com.validator.demo.service;

import com.validator.demo.dto.ValidatorResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ValidatorServiceTest {

    private final ValidatorPasswordService service = new ValidatorPasswordService();

    @Test
    void validPassword_shouldBeValid() {
        ValidatorResponseDTO resp = service.execute("Abcdef1!");
        Assertions.assertTrue(resp.valid());
        Assertions.assertTrue(resp.errors().isEmpty());
    }

    @Test
    void invalidPassword_shouldReturnErrors() {
        ValidatorResponseDTO resp = service.execute("abc");
        Assertions.assertFalse(resp.valid());
        List<String> errors = resp.errors();
        Assertions.assertFalse(errors.isEmpty());
        // Expect at least length and uppercase and digit and special char
        Assertions.assertTrue(errors.stream().anyMatch(s -> s.toLowerCase().contains("at least 8")));
    }
}

