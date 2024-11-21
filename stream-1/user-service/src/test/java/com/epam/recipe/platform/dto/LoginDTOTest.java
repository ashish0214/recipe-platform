package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.dto.request.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidLoginDTO() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john.doe@example.com");
        loginDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmailNotNull() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(null);
        loginDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertEquals(1, violations.size());
        assertEquals("Email cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailValid() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("invalid-email");
        loginDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordNotBlank() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john.doe@example.com");
        loginDTO.setPassword("");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertEquals(1, violations.size());
        assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
    }
}
