package com.epam.recipe.platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import static com.epam.recipe.platform.constants.UserConstant.*;

@Getter
@Setter
public class VerifyOtpRequestDTO {

    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = EMAIL_VALID_MESSAGE
    )
    @Email(message = EMAIL_VALID_MESSAGE)
    @NotBlank(message = EMAIL_NON_NULL_MESSAGE)
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotNull(message = OTP_NON_NULL_MESSAGE)
    @Digits(integer = 6, fraction = 0, message = OTP_DIGITS_MESSAGE)
    @Min(value = 100000, message = OTP_SIZE_MESSAGE)
    @Max(value = 999999, message = OTP_SIZE_MESSAGE)
    @Schema(description = "OTP (One Time Password) for verification", example = "123456")
    private Integer otp;
}