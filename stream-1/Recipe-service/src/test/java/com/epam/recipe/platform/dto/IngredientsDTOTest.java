package com.epam.recipe.platform.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientsDTOTest
{
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName("Salt")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testNullIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName(null)
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testEmptyIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName("")
                .build();

        assertThat(validator.validate(dto)).hasSize(3); // 3 constraints violations
    }

    @Test
    void testMinLengthIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName("Ab")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testMaxLengthIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName("ValidName")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testExceedMaxLengthIngredientName() {
        IngredientsDTO dto = IngredientsDTO.builder()
                .ingredientName("ThisIngredientNameExceedsMaximumAllowedLength")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }
}
