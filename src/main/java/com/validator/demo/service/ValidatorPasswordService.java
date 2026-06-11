package com.validator.demo.service;

import com.validator.demo.dto.ValidatorResponseDTO;
import com.validator.demo.model.interfaces.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ValidatorPasswordService implements Validator {

    private static final Map<Predicate<String>, String> RULES = Map.ofEntries(
            Map.entry(p -> p != null && p.length() >= 8, "A senha deve ter pelo menos 8 caracteres"),
            Map.entry(p -> p != null && p.matches(".*[A-Z].*"), "A senha deve conter pelo menos uma letra maiúscula"),
            Map.entry(p -> p != null && p.matches(".*[a-z].*"), "A senha deve conter pelo menos uma letra minuscula"),
            Map.entry(p -> p != null && p.matches(".*\\d.*"), "A senha deve conter pelo menos um digito"),
            Map.entry(p -> p != null && p.matches(".*[^A-Za-z0-9].*"), "A senha deve conter pelo menos um caractere especial")
    );

    @Override
    public ValidatorResponseDTO execute(String value) {
        List<String> errors = RULES.entrySet().stream()
                .filter(e -> !e.getKey().test(value))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        boolean valid = errors.isEmpty();
        return new ValidatorResponseDTO(valid, errors);
    }
}
