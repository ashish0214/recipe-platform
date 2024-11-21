package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import com.epam.recipe.platform.dto.RecipeResponseDTO;
import com.epam.recipe.platform.entity.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeResponseDTO entityToRecipeResponseDTO(RecipeEntity recipeEntity);
    RecipeEntity recipeRequestDtoToEntity(RecipeRequestDTO recipeRequestDTO);

}
