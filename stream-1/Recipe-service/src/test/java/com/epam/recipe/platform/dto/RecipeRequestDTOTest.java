package com.epam.recipe.platform.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO();

        // Then
        assertNotNull(recipeRequestDTO);
        assertNull(recipeRequestDTO.getRecipeName());
        assertNull(recipeRequestDTO.getImageToken());
        assertNull(recipeRequestDTO.getRecipeDescription());
        assertNull(recipeRequestDTO.getCuisine());
        assertNull(recipeRequestDTO.getCategory());
        assertNull(recipeRequestDTO.getTags());
        assertNull(recipeRequestDTO.getIngredients());
        assertNull(recipeRequestDTO.getCookingTime());
        assertNull(recipeRequestDTO.getDifficulty());
        assertNull(recipeRequestDTO.getDietaryRestrictions());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        String recipeId = "123";
        String recipeName = "Test Recipe";
        String imageToken = "token123";
        String recipeDescription = "Test recipe description";
        String cuisine = "Test cuisine";
        String category = "Test category";
        List<String> tags = Arrays.asList("tag1", "tag2");
        List<String> ingredients = Arrays.asList("ingredient1", "ingredient2");
        Integer cookingTime = 30;
        String difficulty = "Easy";

        // When
        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO( recipeName, imageToken, recipeDescription, cuisine,
                category, tags, ingredients, cookingTime, difficulty,null);

        // Then
        assertEquals(recipeName, recipeRequestDTO.getRecipeName());
        assertEquals(imageToken, recipeRequestDTO.getImageToken());
        assertEquals(recipeDescription, recipeRequestDTO.getRecipeDescription());
        assertEquals(cuisine, recipeRequestDTO.getCuisine());
        assertEquals(category, recipeRequestDTO.getCategory());
        assertEquals(tags, recipeRequestDTO.getTags());
        assertEquals(ingredients, recipeRequestDTO.getIngredients());
        assertEquals(cookingTime, recipeRequestDTO.getCookingTime());
        assertEquals(difficulty, recipeRequestDTO.getDifficulty());
        assertNull(recipeRequestDTO.getDietaryRestrictions());
    }

    @Test
    void testBuilder() {
        // When
        RecipeRequestDTO recipeRequestDTO = RecipeRequestDTO.builder()
                .recipeName("Test Recipe")
                .imageToken("token123")
                .recipeDescription("Test recipe description")
                .cuisine("Test cuisine")
                .category("Test category")
                .tags(Arrays.asList("tag1", "tag2"))
                .ingredients(Arrays.asList("ingredient1", "ingredient2"))
                .cookingTime(30)
                .difficulty("Easy")
                .build();

        // Then
        assertNotNull(recipeRequestDTO);
        assertEquals("Test Recipe", recipeRequestDTO.getRecipeName());
        assertEquals("token123", recipeRequestDTO.getImageToken());
        assertEquals("Test recipe description", recipeRequestDTO.getRecipeDescription());
        assertEquals("Test cuisine", recipeRequestDTO.getCuisine());
        assertEquals("Test category", recipeRequestDTO.getCategory());
        assertEquals(Arrays.asList("tag1", "tag2"), recipeRequestDTO.getTags());
        assertEquals(Arrays.asList("ingredient1", "ingredient2"), recipeRequestDTO.getIngredients());
        assertEquals(30, recipeRequestDTO.getCookingTime());
        assertEquals("Easy", recipeRequestDTO.getDifficulty());
        assertNull(recipeRequestDTO.getDietaryRestrictions());
    }


    @Test
    void testGetterAndSetter() {
        // Given
        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO();

        // When
        recipeRequestDTO.setRecipeName("Test Recipe");
        recipeRequestDTO.setImageToken("token123");
        recipeRequestDTO.setRecipeDescription("Test recipe description");
        recipeRequestDTO.setCuisine("Test cuisine");
        recipeRequestDTO.setCategory("Test category");
        recipeRequestDTO.setTags(Arrays.asList("tag1", "tag2"));
        recipeRequestDTO.setIngredients(Arrays.asList("ingredient1", "ingredient2"));
        recipeRequestDTO.setCookingTime(30);
        recipeRequestDTO.setDifficulty("Easy");

        // Then
        assertEquals("Test Recipe", recipeRequestDTO.getRecipeName());
        assertEquals("token123", recipeRequestDTO.getImageToken());
        assertEquals("Test recipe description", recipeRequestDTO.getRecipeDescription());
        assertEquals("Test cuisine", recipeRequestDTO.getCuisine());
        assertEquals("Test category", recipeRequestDTO.getCategory());
        assertEquals(Arrays.asList("tag1", "tag2"), recipeRequestDTO.getTags());
        assertEquals(Arrays.asList("ingredient1", "ingredient2"), recipeRequestDTO.getIngredients());
        assertEquals(30, recipeRequestDTO.getCookingTime());
        assertEquals("Easy", recipeRequestDTO.getDifficulty());
        assertNull(recipeRequestDTO.getDietaryRestrictions());
    }
}
