package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.IngredientsDTO;
import com.epam.recipe.platform.entity.IngredientsEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.IngredientRepository;
import com.epam.recipe.platform.service.IngredientService;
import com.epam.recipe.platform.utility.IngredientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public IngredientsDTO getIngredientByName(String name) {
        log.info("Searching for ingredient by name: {}", name);
        IngredientsEntity ingredientEntity = ingredientRepository.findByIngredientNameIgnoreCase(name)
                .orElseThrow(() -> {
                    log.error("No ingredient found with name: {}", name);
                    return new RecipeException("Ingredient not found", HttpStatus.NOT_FOUND);
                });

        log.info("Found ingredient with name: {}", name);
        return IngredientMapper.INSTANCE.ingredientsEntityToDTO(ingredientEntity);
    }

    @Override
    public List<IngredientsDTO> getIngredients() {
        log.info("Fetching all ingredients");
        return ingredientRepository.findAll().stream()
                .map(IngredientMapper.INSTANCE::ingredientsEntityToDTO)
                .toList();
    }
}
