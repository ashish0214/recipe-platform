package com.epam.recipe.platform.service.impl;


import com.epam.recipe.platform.dto.IngredientsDTO;
import com.epam.recipe.platform.entity.IngredientsEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.IngredientRepository;
import com.epam.recipe.platform.utility.IngredientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientsServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    private IngredientsDTO ingredientsDTO;
    private IngredientsEntity ingredientsEntity;

    @BeforeEach
    void setUp() {

        ingredientsEntity = new IngredientsEntity();
        ingredientsEntity.setIngredientName("Tomato");
        ingredientsEntity.setIngredientId("1");

        ingredientsDTO = IngredientMapper.INSTANCE.ingredientsEntityToDTO(ingredientsEntity);

    }

    @Test
    void testGetIngredientByName_ValidName() {
        when(ingredientRepository.findByIngredientNameIgnoreCase("Tomato")).thenReturn(Optional.of(ingredientsEntity));
         ingredientsDTO = ingredientService.getIngredientByName("Tomato");
        assertEquals("1", ingredientsDTO.getIngredientId());
        assertEquals("Tomato", ingredientsDTO.getIngredientName());
        verify(ingredientRepository, times(1)).findByIngredientNameIgnoreCase("Tomato");
    }

    @Test
    void testGetIngredientByName_InvalidName() {
        when(ingredientRepository.findByIngredientNameIgnoreCase("Invalid")).thenReturn(Optional.empty());
        assertThrows(RecipeException.class, () -> ingredientService.getIngredientByName("Invalid"));
    }

    @Test
    void testGetIngredients_MultipleIngredients() {
        IngredientsEntity entity1 = new IngredientsEntity();
        IngredientsEntity entity2 = new IngredientsEntity();
        when(ingredientRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        assertEquals(2, ingredientService.getIngredients().size());
    }

    @Test
    void testGetIngredients_NoIngredients() {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0, ingredientService.getIngredients().size());
    }


}