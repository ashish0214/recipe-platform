package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.utility.UsedByReflection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {

    @UsedByReflection
    private String recipeId;

    @UsedByReflection
    private String recipeName;

    @UsedByReflection
    private String imageToken;

    @UsedByReflection
    private String recipeDescription;

    @UsedByReflection
    private String cuisine;

    @UsedByReflection
    private String category;

    @UsedByReflection
    private List<String> tags;

    @UsedByReflection
    private List<String> ingredients;

    @UsedByReflection
    private Integer cookingTime;

    @UsedByReflection
    private String difficulty;

    @UsedByReflection
    private String dietaryRestrictions;

    @UsedByReflection
    private List<String> reviews;
    @UsedByReflection
    private Double ratings;
    @UsedByReflection
    private Integer countOfRatings;
}
