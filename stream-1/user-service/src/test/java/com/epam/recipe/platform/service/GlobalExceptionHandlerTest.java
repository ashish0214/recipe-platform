package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.response.ErrorResponseDTO;
import com.epam.recipe.platform.exceptionhandler.GlobalExceptionHandler;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleMethodArgumentNotValid() {
        BindingResult bindingResult = new BindException(new Object(), "object");
        bindingResult.addError(new FieldError("object", "field", "Validation error"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        java.util.Map<String, String> validationErrors = (java.util.Map<String, String>) response.getBody();
        assertEquals(1, validationErrors.size());
        assertEquals("Validation error", validationErrors.get("field"));
    }

    @Test
    void otpException() {
        OtpException exception = new OtpException("OTP error", HttpStatus.BAD_REQUEST);
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.otpException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("OTP error", Objects.requireNonNull(response.getBody()).errorMessage());
    }

    @Test
    void sqlException() {
        SQLIntegrityConstraintViolationException exception = new SQLIntegrityConstraintViolationException("SQL error");
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.sqlException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("SQL error", Objects.requireNonNull(response.getBody()).errorMessage());
    }

    @Test
    void userException() {
        UserException exception = new UserException("User error", HttpStatus.BAD_REQUEST);
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.userException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User error", Objects.requireNonNull(response.getBody()).errorMessage());
    }

    @Test
    void getErrorResponse() {
        Exception exception = new Exception("Error message");
        ErrorResponseDTO errorResponse = globalExceptionHandler.getErrorResponse(exception);
        assertEquals("Error message", errorResponse.errorMessage());
    }
}