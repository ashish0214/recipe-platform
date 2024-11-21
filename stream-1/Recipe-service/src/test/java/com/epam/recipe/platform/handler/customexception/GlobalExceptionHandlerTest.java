package com.epam.recipe.platform.handler.customexception;

import com.epam.recipe.platform.dto.error.ErrorResponseDTO;
import com.epam.recipe.platform.handler.GlobalExceptionHandler;
import com.epam.recipe.platform.handler.exception.RecipeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setup() {
        reset(bindingResult);
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertEquals(2, responseBody.size());
        assertEquals("Error message 1", responseBody.get("fieldName1"));
        assertEquals("Error message 2", responseBody.get("fieldName2"));
    }

    @Test
     void testHandleMethodArgumentNotValid_NoErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of());
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertEquals(0, responseBody.size());
    }

    @Test
    void testRecipeException() {
        // Mock RecipeException
        RecipeException ex = new RecipeException("Recipe not found",HttpStatus.BAD_REQUEST);
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.recipeNotFoundException(ex);
        // Verify the response entity and content
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Recipe not found", responseEntity.getBody().getErrorMessage());
        // You can add more assertions for timestamp verification if needed
    }

    @Test
     void testRuntimeException() {
        // Mock RuntimeException
        RuntimeException ex = new RuntimeException("Internal Server Error");

        // Invoke handler method
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.runtimeException(ex);

        // Verify the response entity and content
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal Server Error", responseEntity.getBody().getErrorMessage());
        // You can add more assertions for timestamp verification if needed
    }
}
