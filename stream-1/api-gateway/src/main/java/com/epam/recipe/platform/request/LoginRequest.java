package com.epam.recipe.platform.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}