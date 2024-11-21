package com.epam.recipe.platform.dto;

import com.epam.recipe.platform.utility.UsedByReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.epam.recipe.platform.constants.RecipeDTOValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("unused")
public class IngredientsDTO
{
    @UsedByReflection
    private String ingredientId;

    @NotBlank(message = INGREDIENTS_NOT_EMPTY_MESSAGE)
    @Size(min = 3, max = 30, message = INGREDIENTS_SIZE_MESSAGE)
    @Pattern(regexp = "^(?![ ]+$)[a-zA-Z]+([ -][a-zA-Z]+)*$", message = INGREDIENTS_PATTERN_MESSAGE)
    private String ingredientName;
}
