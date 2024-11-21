package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.dto.request.ResetPasswordRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static com.epam.recipe.platform.constants.UserConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResetPasswordRequestDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidResetPasswordRequestDTO() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail("john.doe@example.com");
        resetPasswordRequestDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<ResetPasswordRequestDTO>> violations = validator.validate(resetPasswordRequestDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmailNotNull() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail(null);
        resetPasswordRequestDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<ResetPasswordRequestDTO>> violations = validator.validate(resetPasswordRequestDTO);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_NON_NULL_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailValid() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail("invalid-email");
        resetPasswordRequestDTO.setPassword("P@ssw0rd");

        Set<ConstraintViolation<ResetPasswordRequestDTO>> violations = validator.validate(resetPasswordRequestDTO);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_VALID_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordNotBlank() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail("john.doe@example.com");
        resetPasswordRequestDTO.setPassword("a");

        Set<ConstraintViolation<ResetPasswordRequestDTO>> violations = validator.validate(resetPasswordRequestDTO);
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter, and one special character, and must not contain whitespace", violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordPattern() {
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail("john.doe@example.com");
        resetPasswordRequestDTO.setPassword("invalid");

        Set<ConstraintViolation<ResetPasswordRequestDTO>> violations = validator.validate(resetPasswordRequestDTO);
        assertEquals(1, violations.size());
        assertEquals(PASSWORD_VALID_MESSAGE, violations.iterator().next().getMessage());
    }
}

