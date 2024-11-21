package com.epam.recipe.platform.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecipeResponseDTOTest {

    private RecipeResponseDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new RecipeResponseDTO();
    }

    // Constructor tests

    @Test
    public void testNoArgsConstructor() {
        assertNotNull(dto);
    }

    @Test
    public void testAllArgsConstructor() {
        List<String> tags = Arrays.asList("pasta", "cheese");
        List<String> ingredients = Arrays.asList("pasta", "cheese", "tomato sauce");

        dto = new RecipeResponseDTO(
                "1",
                "Lasagna",
                "token123",
                "Delicious Italian dish",
                "Italian",
                "Main Course",
                tags,
                ingredients,
                60,
                "Medium",
                "No dietary restrictions",
                Arrays.asList("Great recipe!", "Delicious!"),
                4.5,
                10
        );

        assertNotNull(dto);
        assertEquals("Lasagna", dto.getRecipeName());
        assertEquals(60, dto.getCookingTime());
        assertEquals(Arrays.asList("Great recipe!", "Delicious!"), dto.getReviews());
        assertEquals(4.5, dto.getRatings());
    }

    // Builder pattern test

    @Test
    public void testBuilder() {
        List<String> tags = Arrays.asList("pasta", "cheese");
        List<String> ingredients = Arrays.asList("pasta", "cheese", "tomato sauce");

        dto = RecipeResponseDTO.builder()
                .recipeName("Spaghetti Carbonara")
                .cuisine("Italian")
                .tags(tags)
                .ingredients(ingredients)
                .cookingTime(30)
                .difficulty("Medium")
                .build();

        assertNotNull(dto);
        assertEquals("Spaghetti Carbonara", dto.getRecipeName());
        assertEquals("Italian", dto.getCuisine());
        assertEquals(30, dto.getCookingTime());
        assertEquals(Arrays.asList("pasta", "cheese"), dto.getTags());
        assertEquals("Medium", dto.getDifficulty());
    }

    // Getter and setter tests

    @Test
    public void testGetterAndSetter() {
        dto.setRecipeName("Risotto");
        dto.setCategory("Main Course");
        dto.setCookingTime(45);
        dto.setRatings(4.0);

        assertEquals("Risotto", dto.getRecipeName());
        assertEquals("Main Course", dto.getCategory());
        assertEquals(45, dto.getCookingTime());
        assertEquals(4.0, dto.getRatings());
    }


}
