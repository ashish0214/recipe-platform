package com.epam.recipe.platform.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CuisineDTOTest
{
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName("Salt")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testNullIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName(null)
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testEmptyIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName(" ")
                .build();
        assertThat(validator.validate(dto)).hasSize(3);
    }

    @Test
    void testMinLengthIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName("A")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testMaxLengthIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName("ValidName")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testExceedMaxLengthIngredientName() {
        CuisineDTO dto = CuisineDTO.builder()
                .cuisineName("ThisIngredientNameExceedsMaximumAllowedLength")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }
}