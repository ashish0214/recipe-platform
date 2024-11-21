package com.epam.recipe.platform.handler.customexception;

import com.epam.recipe.platform.handler.exception.RecipeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeExceptionTest {
    @Test
    void testExceptionMessage() {
        String expectedMessage = "This is a test message";
        RecipeException exception = new RecipeException(expectedMessage, null);
        assertEquals(expectedMessage, exception.getMessage());
    }
}