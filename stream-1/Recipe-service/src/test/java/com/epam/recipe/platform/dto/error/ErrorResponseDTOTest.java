package com.epam.recipe.platform.dto.error;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseDTOTest {

    @Test
    void testBuilder() {
        // Given
        LocalDateTime testTime = LocalDateTime.now();
        String testErrorMessage = "Test error message";

        // When
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .errorMessage(testErrorMessage)
                .build();

        // Then
        assertEquals(testErrorMessage, errorResponseDTO.getErrorMessage());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();

        // Then
        assertNotNull(errorResponseDTO);
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        String testErrorMessage = "Test error message";
        LocalDateTime testTime = LocalDateTime.now();

        // When
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(testErrorMessage);

        // Then
        assertEquals(testErrorMessage, errorResponseDTO.getErrorMessage());

    }

    @Test
    void testGettersAndSetters() {
        // Given
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        LocalDateTime testTime = LocalDateTime.now();
        String testErrorMessage = "Test error message";

        // When
        errorResponseDTO.setErrorMessage(testErrorMessage);

        // Then
        assertEquals(testErrorMessage, errorResponseDTO.getErrorMessage());

    }
}
