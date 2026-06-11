package com.validator.demo.controller;


import com.validator.demo.dto.ValidatorRequestDTO;
import com.validator.demo.dto.ValidatorResponseDTO;
import com.validator.demo.model.interfaces.Validator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/validators")
public class ValidatorController {

    private final Validator validator;

    public ValidatorController(Validator validator) {
        this.validator = validator;
    }

    @GetMapping("/password")
    public String showForm(Model model) {
        model.addAttribute("request", new ValidatorRequestDTO(""));
        return "password-form";
    }

    @PostMapping("/password-validate")
    public String validatePassword(@Valid @ModelAttribute("request") ValidatorRequestDTO request,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("response", new ValidatorResponseDTO(false, List.of("A senha não pode está em branco")));
            return "password-form";
        }

        ValidatorResponseDTO response = this.validator.execute(request.value());
        model.addAttribute("response", response);
        return "password-form";
    }
}
