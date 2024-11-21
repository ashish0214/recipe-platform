package com.epam.recipe.platform.dto.error;
import com.epam.recipe.platform.utility.UsedByReflection;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    @UsedByReflection
    private String errorMessage;
}
