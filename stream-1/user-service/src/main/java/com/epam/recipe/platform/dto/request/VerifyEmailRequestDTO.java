package com.epam.recipe.platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.epam.recipe.platform.constants.UserConstant.*;

@Getter
@Setter
public class VerifyEmailRequestDTO {

    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = EMAIL_VALID_MESSAGE
    )
    @Email(message = EMAIL_VALID_MESSAGE)
    @NotBlank(message = EMAIL_NON_NULL_MESSAGE)
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;
}