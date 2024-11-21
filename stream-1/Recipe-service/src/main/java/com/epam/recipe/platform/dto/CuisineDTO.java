package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.utility.UsedByReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.epam.recipe.platform.constants.RecipeDTOValidationConstants.RECIPE_CUISINE_PATTERN_MESSAGE;
import static com.epam.recipe.platform.constants.RecipeDTOValidationConstants.RECIPE_CUISINE_SIZE_MESSAGE;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuisineDTO {
    @UsedByReflection
    private String id;

    @NotBlank(message = "Cuisine name should not be blank.")
    @Size(min = 2, max = 20, message = RECIPE_CUISINE_SIZE_MESSAGE)
    @Pattern(regexp = "^(?![ ]+$)[a-zA-Z]+([ -][a-zA-Z]+)*$", message = RECIPE_CUISINE_PATTERN_MESSAGE)
    private String cuisineName;
}
