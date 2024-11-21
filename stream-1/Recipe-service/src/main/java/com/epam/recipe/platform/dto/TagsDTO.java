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
public class TagsDTO
{
    @UsedByReflection
    private String id;

    @NotBlank(message = TAGS_NOT_EMPTY_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = TAGS_SIZE_MESSAGE)
    @Size(min = 2, max = 35, message = TAGS_PATTERN_MESSAGE)
    private String tagName;
}
