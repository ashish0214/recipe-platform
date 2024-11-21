package com.epam.recipe.platform.utility;


import com.epam.recipe.platform.dto.IngredientsDTO;
import com.epam.recipe.platform.entity.IngredientsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper
{
    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    IngredientsDTO ingredientsEntityToDTO(IngredientsEntity ingredients);

}
