package com.epam.recipe.platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.epam.recipe.platform.constants.UserConstant.*;

@Getter
@Setter
@Schema(description = "Data Transfer Object for user registration details")
public class UserDTO {

    @NotBlank(message =FIRST_NAME_NOT_BLANK_MESSAGE)
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotBlank(message =LAST_NAME_NON_BLANK_MESSAGE)
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = EMAIL_VALID_MESSAGE
    )
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = PASSWORD_NON_NULL_MESSAGE)
    @Pattern(regexp = PASSWORD_VALIDATION_EXPRESSION,
            message = PASSWORD_VALID_MESSAGE )
    @Schema(description = "Password of the user", example = "P@ssw0rd")
    private String password;

    @Schema(description = "OTP sent to the user's email address", example = "123456")
    @NotNull(message = OTP_NON_NULL_MESSAGE)
    private Integer otp;


}