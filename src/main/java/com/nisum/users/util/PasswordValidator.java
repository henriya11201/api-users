package com.nisum.users.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PasswordValidator {

    @Autowired
    @Value("${pattern.password.regexp}")
    private String PATTERN_PASSWORD;

    public void validatePassword(String password) {
        if (!password.matches(PATTERN_PASSWORD)) {
            throw new ValidationException("La contraseña no cumple con los requisitos:\n" +
                    "- Debe tener al menos 8 caracteres.\n" +
                    "- Debe incluir al menos una letra mayúscula.\n" +
                    "- Debe incluir al menos una letra minúscula.\n" +
                    "- Debe contener al menos un dígito.\n" +
                    "- Debe incluir al menos un carácter especial (por ejemplo, !@#$%^&*).");
        }
    }
}
