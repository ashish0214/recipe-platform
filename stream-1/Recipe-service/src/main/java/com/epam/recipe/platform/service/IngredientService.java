package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.IngredientsDTO;

import java.util.List;

public interface IngredientService
{
    IngredientsDTO getIngredientByName(String name) ;

    List<IngredientsDTO> getIngredients();

}
