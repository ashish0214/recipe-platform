package com.epam.recipe.platform.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.fail;


public class RecipeDTOValidationConstantsTest {

    @Test
    public void testPrivateConstructor() {
        // Use reflection to access the private constructor
        Constructor<RecipeDTOValidationConstants> constructor = null;
        try {
            constructor = RecipeDTOValidationConstants.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Ensure constructor is accessible (even though it's private)
        constructor.setAccessible(true);

        // Attempt to instantiate the class
        try {
            constructor.newInstance();
            // If instantiation succeeds unexpectedly, fail the test
            fail("Expected AssertionError not thrown");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Expected AssertionError should be thrown, test passes
            if (!(e.getCause() instanceof AssertionError)) {
                fail("Expected AssertionError not thrown");
            }
        }
    }
}
