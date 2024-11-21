package com.epam.recipe.platform.api;

import com.epam.recipe.platform.dto.request.LoginDTO;
import com.epam.recipe.platform.dto.request.ResetPasswordRequestDTO;
import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.dto.response.LoginResponseDTO;
import com.epam.recipe.platform.dto.response.ResponseDTO;
import com.epam.recipe.platform.service.TokenService;
import com.epam.recipe.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.epam.recipe.platform.constants.UserConstant.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Register a new user", description = "This endpoint registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = USER_REGISTRATION_SUCCESS_MESSAGE),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE),
            @ApiResponse(responseCode = "409", description = EMAIL_ALREADY_EXISTS_MESSAGE)
    })
    public ResponseEntity<ResponseDTO> register(
            @ModelAttribute @Valid
            UserDTO userDTO,
            @RequestParam( value = "image",required = false)
            MultipartFile imageFile) {
        log.info("Entered into register() method with request {}", userDTO.toString());
        userService.register(userDTO, imageFile);
        log.info("Exited from userRegistration method");
        return new ResponseEntity<>(new ResponseDTO(USER_REGISTRATION_SUCCESS_MESSAGE), HttpStatus.CREATED);
    }

    @PostMapping("login")
    @Operation(summary = "User Login", description = "This endpoint validates the user credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    public ResponseEntity<ResponseDTO> userLogin(@Valid @RequestBody LoginDTO loginDto) {
        log.info("User login request received");
        return ResponseEntity.ok(new ResponseDTO(userService.login(loginDto)));
    }

    @Operation(summary = "Reset Password",
            description = "Reset the password for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Email already sent")
    })
    @PatchMapping("reset-password")
    public ResponseEntity<LoginResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO)
    {
        log.info("Attempting to change password for email: {}", resetPasswordRequestDTO.getEmail());
        userService.resetPassword(resetPasswordRequestDTO.getPassword(), resetPasswordRequestDTO.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(PASSWORD_CHANGED_MESSAGE));
    }

    @Operation(summary = "Validate Token",
            description = "Validate the jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/validate-token")
    public Boolean validateToken(@RequestParam("token") String token) {
        log.info("Entered validate token method at "+ LocalDateTime.now());
        return tokenService.validateToken(token);
    }
}