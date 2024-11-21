package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.constants.RecipeDTOValidationConstants;
import com.epam.recipe.platform.utility.UsedByReflection;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequestDTO {

    @NotNull(message = RecipeDTOValidationConstants.RECIPE_NAME_NOT_NULL_MESSAGE)
    @Size(min = 2, max = 50, message = RecipeDTOValidationConstants.RECIPE_NAME_SIZE_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9\\- ]*$", message = RecipeDTOValidationConstants.RECIPE_NAME_PATTERN_MESSAGE)
    private String recipeName;

    @UsedByReflection
    private String imageToken;

    @Size(min = 20, max = 5000, message = RecipeDTOValidationConstants.RECIPE_DESCRIPTION_SIZE_MESSAGE)
    @NotNull(message = RecipeDTOValidationConstants.RECIPE_DESCRIPTION_NOT_NULL_MESSAGE)
    private String recipeDescription;

    @Size(min = 2, max = 20, message = RecipeDTOValidationConstants.RECIPE_CUISINE_SIZE_MESSAGE)
    @NotNull(message = RecipeDTOValidationConstants.RECIPE_CUISINE_NOT_NULL_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\- ]*$", message = RecipeDTOValidationConstants.RECIPE_CUISINE_PATTERN_MESSAGE)
    private String cuisine;

    @Size(min = 2, max = 20, message = RecipeDTOValidationConstants.RECIPE_CATEGORY_SIZE_MESSAGE)
    @NotNull(message = RecipeDTOValidationConstants.RECIPE_CATEGORY_NOT_NULL_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\- ]*$", message = RecipeDTOValidationConstants.RECIPE_CATEGORY_PATTERN_MESSAGE)
    private String category;

    @NotEmpty(message = RecipeDTOValidationConstants.TAGS_NOT_EMPTY_MESSAGE)
    @Size(min = 1, max = 10, message = RecipeDTOValidationConstants.TAGS_SIZE_MESSAGE)
    private List<String> tags;

    @NotEmpty(message = RecipeDTOValidationConstants.INGREDIENTS_NOT_EMPTY_MESSAGE)
    @Size(min = 1, max = 10, message = RecipeDTOValidationConstants.INGREDIENTS_SIZE_MESSAGE)
    private List<String> ingredients;

    @NotNull(message = RecipeDTOValidationConstants.COOKING_TIME_NOT_NULL_MESSAGE)
    @Min(value = 1, message = RecipeDTOValidationConstants.COOKING_TIME_MIN_MESSAGE)
    @Max(value = 180, message = RecipeDTOValidationConstants.COOKING_TIME_MAX_MESSAGE)
    private Integer cookingTime;

    @Size(min = 2, max = 50, message = RecipeDTOValidationConstants.DIFFICULTY_SIZE_MESSAGE)
    @NotNull(message = RecipeDTOValidationConstants.DIFFICULTY_NOT_NULL_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ]*$", message = RecipeDTOValidationConstants.DIFFICULTY_PATTERN_MESSAGE)
    private String difficulty;

    @UsedByReflection
    private String dietaryRestrictions;

}
