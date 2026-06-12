package com.validator.demo.service;

import com.validator.demo.dto.ValidatorResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidatorPasswordServiceTest {

    private ValidatorPasswordService service;

    @BeforeEach
    void setUp() {
        service = new ValidatorPasswordService();
    }

    @Test
    void validPassword_shouldBeValid() {
        String password = "AbcDef1$9";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertTrue(resp.valid());
        Assertions.assertTrue(resp.errors().isEmpty());
    }

    @Test
    void tooShort_shouldReturnLengthError() {
        String password = "Abc1$9";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertFalse(resp.valid());
        Assertions.assertTrue(resp.errors().stream().anyMatch(s -> s.contains("pelo menos 9")));
    }

    @Test
    void missingUppercase_shouldReturnUpperError() {
        String password = "abcdef1$9";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertFalse(resp.valid());
        Assertions.assertTrue(resp.errors().stream().anyMatch(s -> s.contains("maiúscula")));
    }

    @Test
    void missingSpecial_shouldReturnSpecialError() {
        String password = "AbcDef123";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertFalse(resp.valid());
        Assertions.assertTrue(resp.errors().stream().anyMatch(s -> s.contains("caractere especial")));
    }

    @Test
    void containsWhitespace_shouldReturnWhitespaceError() {
        String password = "AbcDef1 9";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertFalse(resp.valid());
        Assertions.assertTrue(resp.errors().stream().anyMatch(s -> s.contains("espaços")));
    }

    @Test
    void repeatedCharacter_shouldReturnDuplicateError() {
        String password = "AbcDef1$A";
        ValidatorResponseDTO resp = service.execute(password);
        Assertions.assertFalse(resp.valid());
        Assertions.assertTrue(resp.errors().stream().anyMatch(s -> s.contains("repetidos")));
    }

    @Test
    void nullPassword_shouldThrow() {
        Assertions.assertThrows(NullPointerException.class, () -> service.execute(null));
    }
}

