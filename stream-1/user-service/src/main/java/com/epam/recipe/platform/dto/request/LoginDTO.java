package com.epam.recipe.platform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}

