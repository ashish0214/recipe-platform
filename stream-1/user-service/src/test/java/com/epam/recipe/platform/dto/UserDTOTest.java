package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.dto.request.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import java.util.Set;

import static com.epam.recipe.platform.constants.UserConstant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("P@ssw0rd");
        userDTO.setOtp(123456);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testFirstNameNotBlank() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("P@ssw0rd");
        userDTO.setOtp(123456);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertEquals(1, violations.size());
        assertEquals(FIRST_NAME_NOT_BLANK_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testLastNameNotBlank() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("P@ssw0rd");
        userDTO.setOtp(123456);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertEquals(1, violations.size());
        assertEquals(LAST_NAME_NON_BLANK_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailPattern() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("invalid-email");
        userDTO.setPassword("P@ssw0rd");
        userDTO.setOtp(123456);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertEquals(1, violations.size());
        assertEquals(EMAIL_VALID_MESSAGE, violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordPattern() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("invalid");
        userDTO.setOtp(123456);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertEquals(1, violations.size());
        assertEquals(PASSWORD_VALID_MESSAGE, violations.iterator().next().getMessage());
    }
}

