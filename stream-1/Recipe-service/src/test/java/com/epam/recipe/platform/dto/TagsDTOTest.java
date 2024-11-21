package com.epam.recipe.platform.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagsDTOTest
{
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName("Salt")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testNullIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName(null)
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testEmptyIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName(" ")
                .build();
        assertThat(validator.validate(dto)).hasSize(3);
    }

    @Test
    void testMinLengthIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName("A")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void testMaxLengthIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName("ValidName")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void testExceedMaxLengthIngredientName() {
        TagsDTO dto = TagsDTO.builder()
                .tagName("ThisIngredientNameExceedsMaximumAllowedLength")
                .build();

        assertThat(validator.validate(dto)).hasSize(1);
    }
}