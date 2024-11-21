package com.epam.recipe.platform.exceptionhandler;

import com.epam.recipe.platform.dto.response.ErrorResponseDTO;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrorMap = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        validationErrorList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrorMap.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpException.class)
    public  ResponseEntity<ErrorResponseDTO> otpException(OtpException exception) {
        return new ResponseEntity<>(getErrorResponse(exception), exception.getStatus());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public  ResponseEntity<ErrorResponseDTO> sqlException(SQLIntegrityConstraintViolationException exception) {
        return new ResponseEntity<>(getErrorResponse(exception), HttpStatus.CONFLICT);
    }

    public ErrorResponseDTO getErrorResponse(Exception exception){
        return new ErrorResponseDTO(exception.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public  ResponseEntity<ErrorResponseDTO> userException(UserException exception) {
        return new ResponseEntity<>(getErrorResponse(exception), exception.getStatus());
    }

}
