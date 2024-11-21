package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import com.epam.recipe.platform.handler.exception.RecipeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DTOValidationUtilityTest {

    @Mock
    private Validator validator;

    private DTOValidationUtility dtoValidationUtility;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dtoValidationUtility = new DTOValidationUtility(validator);
    }

    @Test
    public void testValidate_success() throws Exception {
        String recipeStr = "{\"recipeName\":\"Test Recipe\",\"ingredients\":[\"Ingredient1\",\"Ingredient2\"]}";
        when(validator.validate(any(RecipeRequestDTO.class))).thenReturn(Collections.emptySet());

        RecipeRequestDTO result = dtoValidationUtility.validate(recipeStr);

        assertNotNull(result);
        assertEquals("Test Recipe", result.getRecipeName());
        verify(validator, times(1)).validate(any(RecipeRequestDTO.class));
    }

    @Test
    public void testValidate_jsonProcessingException() {
        String recipeStr = "Invalid JSON";
        assertThrows(RecipeException.class, () -> dtoValidationUtility.validate(recipeStr));
    }

    @Test
    public void testValidate_validationError() {
        String recipeStr = "{\"recipeName\":\"Test Recipe\",\"ingredients\":[\"Ingredient1\",\"Ingredient2\"]}";
        ConstraintViolation<RecipeRequestDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Validation error message");
        Set violations = Collections.singleton(violation);
        when(validator.validate(any(RecipeRequestDTO.class))).thenReturn(violations);

        RecipeException exception = assertThrows(RecipeException.class, () -> dtoValidationUtility.validate(recipeStr));
        assertTrue(exception.getMessage().contains("Validation error message"));
    }
}