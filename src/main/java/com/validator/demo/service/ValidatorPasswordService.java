package com.validator.demo.service;

import com.validator.demo.dto.ValidatorResponseDTO;
import com.validator.demo.model.interfaces.Validator;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ValidatorPasswordService implements Validator {

    private static final String SPECIALS = "!@#$%^&*()-+";

    private static final Map<Predicate<String>, String> RULES = Map.ofEntries(
            Map.entry(p -> !p.matches(".*\\s.*"), "A senha não deve conter espaços em branco"),
            Map.entry(p -> p.length() >= 9, "A senha deve ter pelo menos 9 caracteres"),
            Map.entry(p -> p.matches(".*[A-Z].*"), "A senha deve conter pelo menos uma letra maiúscula"),
            Map.entry(p -> p.matches(".*[a-z].*"), "A senha deve conter pelo menos uma letra minúscula"),
            Map.entry(p -> p.matches(".*\\d.*"), "A senha deve conter pelo menos um dígito"),
            Map.entry(p -> p.chars().anyMatch(ch -> SPECIALS.indexOf(ch) >= 0),
                    "A senha deve conter pelo menos 1 caractere especial: " + SPECIALS),
            Map.entry(p -> {
                Set<Integer> seen = new HashSet<>();
                for (int ch : p.chars().toArray()) {
                    if (!seen.add(ch)) {
                        return false;
                    }
                }
                return true;
            }, "A senha não deve conter caracteres repetidos (ex: aa, 11, !!)")
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
