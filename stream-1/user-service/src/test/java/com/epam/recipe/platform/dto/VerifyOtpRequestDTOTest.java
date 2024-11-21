package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.dto.request.VerifyOtpRequestDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class VerifyOtpRequestDTOTest {

    private final static String VALID_EMAIL = "john.doe@example.com";
    private final static Integer VALID_OTP = 123456;

    private final Validator validator;

    public VerifyOtpRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDTO() {
        VerifyOtpRequestDTO dto = new VerifyOtpRequestDTO();
        dto.setEmail(VALID_EMAIL);
        dto.setOtp(VALID_OTP);

        Set<ConstraintViolation<VerifyOtpRequestDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidEmail() {
        VerifyOtpRequestDTO dto = new VerifyOtpRequestDTO();
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<VerifyOtpRequestDTO>> violations = validator.validate(dto);
        assertEquals(3, violations.size(), "Expected validation error for invalid email");
    }

    @Test
    public void testNullEmail() {
        VerifyOtpRequestDTO dto = new VerifyOtpRequestDTO();
        dto.setOtp(VALID_OTP);

        Set<ConstraintViolation<VerifyOtpRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Expected validation error for null email");
    }

    @Test
    public void testInvalidOtpDigits() {
        VerifyOtpRequestDTO dto = new VerifyOtpRequestDTO();
        dto.setEmail(VALID_EMAIL);
        dto.setOtp(12345);

        Set<ConstraintViolation<VerifyOtpRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Expected validation error for invalid OTP digits");
    }

    @Test
    public void testNullOtp() {
        VerifyOtpRequestDTO dto = new VerifyOtpRequestDTO();
        dto.setEmail(VALID_EMAIL);

        Set<ConstraintViolation<VerifyOtpRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Expected validation error for null OTP");
    }
}
