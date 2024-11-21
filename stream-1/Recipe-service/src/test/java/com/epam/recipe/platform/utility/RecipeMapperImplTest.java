package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import com.epam.recipe.platform.dto.RecipeResponseDTO;
import com.epam.recipe.platform.entity.RecipeEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeMapperImplTest {

    private RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);

    @Test
    public void testEntityToRecipeResponseDto() {
        // Mock RecipeEntity
        RecipeEntity entity = mock(RecipeEntity.class);
        when(entity.getRecipeId()).thenReturn(String.valueOf(1l));
        when(entity.getRecipeName()).thenReturn("Test Recipe");
        when(entity.getImageToken()).thenReturn("test_token");
        when(entity.getRecipeDescription()).thenReturn("Test description");
        when(entity.getCuisine()).thenReturn("Test cuisine");
        when(entity.getCategory()).thenReturn("Test category");
        when(entity.getTags()).thenReturn(List.of("Tag1", "Tag2"));
        when(entity.getIngredients()).thenReturn(List.of("Ingredient1", "Ingredient2"));
        when(entity.getCookingTime()).thenReturn(30);
        when(entity.getDifficulty()).thenReturn("Easy");
        when(entity.getDietaryRestrictions()).thenReturn("Vegetarian");
        when(entity.getReviews()).thenReturn(List.of("Good recipe"));
        when(entity.getRatings()).thenReturn(4.5);
        when(entity.getCountOfRatings()).thenReturn(10);

        // Perform mapping
        RecipeResponseDTO responseDTO = mapper.entityToRecipeResponseDTO(entity);

        // Verify the mapping
        assertNotNull(responseDTO);
        assertEquals(entity.getRecipeId(), responseDTO.getRecipeId());
        assertEquals(entity.getRecipeName(), responseDTO.getRecipeName());
        assertEquals(entity.getImageToken(), responseDTO.getImageToken());
        assertEquals(entity.getRecipeDescription(), responseDTO.getRecipeDescription());
        assertEquals(entity.getCuisine(), responseDTO.getCuisine());
        assertEquals(entity.getCategory(), responseDTO.getCategory());
        assertEquals(entity.getTags(), responseDTO.getTags());
        assertEquals(entity.getIngredients(), responseDTO.getIngredients());
        assertEquals(entity.getCookingTime(), responseDTO.getCookingTime());
        assertEquals(entity.getDifficulty(), responseDTO.getDifficulty());
        assertEquals(entity.getDietaryRestrictions(), responseDTO.getDietaryRestrictions());
        assertEquals(entity.getReviews(), responseDTO.getReviews());
        assertEquals(entity.getRatings(), responseDTO.getRatings());
        assertEquals(entity.getCountOfRatings(), responseDTO.getCountOfRatings());
    }

    @Test
    public void testRecipeRequestDtoToEntity() {
        // Mock RecipeRequestDTO
        RecipeRequestDTO requestDTO = mock(RecipeRequestDTO.class);
        when(requestDTO.getRecipeName()).thenReturn("Test Recipe");
        when(requestDTO.getImageToken()).thenReturn("test_token");
        when(requestDTO.getRecipeDescription()).thenReturn("Test description");
        when(requestDTO.getCuisine()).thenReturn("Test cuisine");
        when(requestDTO.getCategory()).thenReturn("Test category");
        when(requestDTO.getTags()).thenReturn(List.of("Tag1", "Tag2"));
        when(requestDTO.getIngredients()).thenReturn(List.of("Ingredient1", "Ingredient2"));
        when(requestDTO.getCookingTime()).thenReturn(30);
        when(requestDTO.getDifficulty()).thenReturn("Easy");
        when(requestDTO.getDietaryRestrictions()).thenReturn("Vegetarian");

        // Perform mapping
        RecipeEntity entity = mapper.recipeRequestDtoToEntity(requestDTO);

        // Verify the mapping
        assertNotNull(entity);
        assertEquals(requestDTO.getRecipeName(), entity.getRecipeName());
        assertEquals(requestDTO.getImageToken(), entity.getImageToken());
        assertEquals(requestDTO.getRecipeDescription(), entity.getRecipeDescription());
        assertEquals(requestDTO.getCuisine(), entity.getCuisine());
        assertEquals(requestDTO.getCategory(), entity.getCategory());
        assertEquals(requestDTO.getTags(), entity.getTags());
        assertEquals(requestDTO.getIngredients(), entity.getIngredients());
        assertEquals(requestDTO.getCookingTime(), entity.getCookingTime());
        assertEquals(requestDTO.getDifficulty(), entity.getDifficulty());
        assertEquals(requestDTO.getDietaryRestrictions(), entity.getDietaryRestrictions());
    }
}
