package com.epam.recipe.platform.handler;


import com.epam.recipe.platform.dto.error.ErrorResponseDTO;
import com.epam.recipe.platform.handler.exception.RecipeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("ALL")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", buildErrorMessage(ex));
        Map<String, String> validationErrorMap = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        validationErrorList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrorMap.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrorMap, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<ErrorResponseDTO> recipeNotFoundException(RecipeException exception) {
        return new ResponseEntity<>(getErrorResponse(exception), exception.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> runtimeException(RuntimeException exception) {
        log.error("RuntimeException occurred: {}", buildErrorMessage(exception));
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponseDTO getErrorResponse(Exception exception) {
        return ErrorResponseDTO.builder()
                .errorMessage(exception.getMessage()).build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        log.error("Constraint violation error occurred: {} {}", constraintViolationException.getMessage(), LocalDateTime.now());
        Map<String, String> validationErrorMap = new HashMap<>();
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        violations.forEach(violation -> {
            String validationMsg = violation.getMessage();
            validationErrorMap.put("errorMessage", validationMsg);
        });
        return new ResponseEntity<>(validationErrorMap, HttpStatus.BAD_REQUEST);
    }

    private String buildErrorMessage(Exception ex) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(ex.getMessage());
        if (ex.getStackTrace() != null) {
            for (StackTraceElement element : ex.getStackTrace()) {
                joiner.add(element.toString());
            }
        }
        return joiner.toString();
    }
}