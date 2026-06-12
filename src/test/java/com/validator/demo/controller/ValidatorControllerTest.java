package com.validator.demo.controller;

import com.validator.demo.dto.ValidatorRequestDTO;
import com.validator.demo.dto.ValidatorResponseDTO;
import com.validator.demo.model.interfaces.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

public class ValidatorControllerTest {

    private ValidatorController controller;

    @BeforeEach
    void setUp() {
        Validator fakeValidator = password -> {
            if (password == null || password.isBlank()) {
                return new ValidatorResponseDTO(false, List.of("A senha é inválida"));
            }

            if ("Valid123$".equals(password)) {
                return new ValidatorResponseDTO(true, List.of());
            }
            return new ValidatorResponseDTO(false, List.of("Erro de validação"));
        };

        controller = new ValidatorController(fakeValidator);
    }

    @Test
    void showForm_shouldReturnViewAndModelContainsRequest() {
        Model model = new ConcurrentModel();
        String view = controller.showForm(model);
        Assertions.assertEquals("password-form", view);
        Object req = model.getAttribute("request");
        Assertions.assertNotNull(req);
        Assertions.assertInstanceOf(ValidatorRequestDTO.class, req);
        ValidatorRequestDTO dto = (ValidatorRequestDTO) req;
        Assertions.assertEquals("", dto.value());
    }

    @Test
    void validatePassword_whenBindingErrors_shouldReturnFormWithErrorResponse() {
        ValidatorRequestDTO request = new ValidatorRequestDTO("");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");

        bindingResult.rejectValue("value", "NotBlank", "A senha não pode está em branco");

        Model model = new ConcurrentModel();
        String view = controller.validatePassword(request, bindingResult, model);

        Assertions.assertEquals("password-form", view);
        Object resp = model.getAttribute("response");
        Assertions.assertNotNull(resp);
        Assertions.assertInstanceOf(ValidatorResponseDTO.class, resp);
        ValidatorResponseDTO dto = (ValidatorResponseDTO) resp;
        Assertions.assertFalse(dto.valid());
        Assertions.assertTrue(dto.errors().stream().anyMatch(s -> s.contains("em branco") || s.contains("inválida")));
    }

    @Test
    void validatePassword_whenValid_shouldReturnResponseValidTrue() {
        ValidatorRequestDTO request = new ValidatorRequestDTO("Valid123$");
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");

        Model model = new ConcurrentModel();
        String view = controller.validatePassword(request, bindingResult, model);

        Assertions.assertEquals("password-form", view);
        Object resp = model.getAttribute("response");
        Assertions.assertNotNull(resp);
        Assertions.assertInstanceOf(ValidatorResponseDTO.class, resp);
        ValidatorResponseDTO dto = (ValidatorResponseDTO) resp;
        Assertions.assertTrue(dto.valid());
        Assertions.assertTrue(dto.errors().isEmpty());
    }
}

