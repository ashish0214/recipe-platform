package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.dto.request.VerifyEmailRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyEmailRequestDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidVerifyEmailRequestDTO() {
        VerifyEmailRequestDTO verifyEmailRequestDTO = new VerifyEmailRequestDTO();
        verifyEmailRequestDTO.setEmail("john.doe@example.com");

        Set<ConstraintViolation<VerifyEmailRequestDTO>> violations = validator.validate(verifyEmailRequestDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        VerifyEmailRequestDTO verifyEmailRequestDTO = new VerifyEmailRequestDTO();
        verifyEmailRequestDTO.setEmail("invalid-email");

        Set<ConstraintViolation<VerifyEmailRequestDTO>> violations = validator.validate(verifyEmailRequestDTO);
        assertEquals(2, violations.size());
    }

    @Test
    public void testEmailNotBlank() {
        VerifyEmailRequestDTO verifyEmailRequestDTO = new VerifyEmailRequestDTO();
        verifyEmailRequestDTO.setEmail("");

        Set<ConstraintViolation<VerifyEmailRequestDTO>> violations = validator.validate(verifyEmailRequestDTO);
        assertEquals(2, violations.size());
    }
}

