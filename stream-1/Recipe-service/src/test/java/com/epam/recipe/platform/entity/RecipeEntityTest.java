package com.epam.recipe.platform.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecipeEntityTest {

    private RecipeEntity entity;

    @BeforeEach
    public void setUp() {
        entity = new RecipeEntity();
    }

    // Constructor tests

    @Test
    public void testNoArgsConstructor() {
        assertNotNull(entity);
    }

    @Test
    public void testAllArgsConstructor() {
        List<String> tags = Arrays.asList("pasta", "cheese");
        List<String> ingredients = Arrays.asList("pasta", "cheese", "tomato sauce");

        entity = new RecipeEntity(
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

        assertNotNull(entity);
        assertEquals("Lasagna", entity.getRecipeName());
        assertEquals(60, entity.getCookingTime());
        assertEquals(Arrays.asList("Great recipe!", "Delicious!"), entity.getReviews());
        assertEquals(4.5, entity.getRatings());
    }

    // Builder pattern test

    @Test
    public void testBuilder() {
        List<String> tags = Arrays.asList("pasta", "cheese");
        List<String> ingredients = Arrays.asList("pasta", "cheese", "tomato sauce");

        entity = RecipeEntity.builder()
                .recipeName("Spaghetti Carbonara")
                .cuisine("Italian")
                .tags(tags)
                .ingredients(ingredients)
                .cookingTime(30)
                .difficulty("Medium")
                .build();

        assertNotNull(entity);
        assertEquals("Spaghetti Carbonara", entity.getRecipeName());
        assertEquals("Italian", entity.getCuisine());
        assertEquals(30, entity.getCookingTime());
        assertEquals(Arrays.asList("pasta", "cheese"), entity.getTags());
        assertEquals("Medium", entity.getDifficulty());
    }

    // Getter and setter tests

    @Test
    public void testGetterAndSetter() {
        entity.setRecipeName("Risotto");
        entity.setCategory("Main Course");
        entity.setCookingTime(45);
        entity.setRatings(4.0);

        assertEquals("Risotto", entity.getRecipeName());
        assertEquals("Main Course", entity.getCategory());
        assertEquals(45, entity.getCookingTime());
        assertEquals(4.0, entity.getRatings());
    }

}
