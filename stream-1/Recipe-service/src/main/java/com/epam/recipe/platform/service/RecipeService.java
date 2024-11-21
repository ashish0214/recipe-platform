package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.RecipeResponseDTO;
import com.epam.recipe.platform.dto.RecipeRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface RecipeService {

    List<RecipeResponseDTO> getAllRecipes();

    RecipeResponseDTO addRecipe(RecipeRequestDTO recipeDTO, MultipartFile imageFile);

    RecipeResponseDTO updateRecipe(String recipeId, RecipeRequestDTO recipeRequestDTO, MultipartFile file);

    RecipeResponseDTO getById(String id);

    List<RecipeResponseDTO> search(String text);

    boolean deleteRecipe(String recipeId);

}
