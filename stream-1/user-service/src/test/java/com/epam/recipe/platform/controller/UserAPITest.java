package com.epam.recipe.platform.controller;

import com.epam.recipe.platform.api.UserAPI;
import com.epam.recipe.platform.dto.request.LoginDTO;
import com.epam.recipe.platform.dto.request.ResetPasswordRequestDTO;
import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.service.TokenService;
import com.epam.recipe.platform.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserAPITest {
    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserAPI userAPI;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userAPI).build();
    }

    @Test
    void testUserRegistrationSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@email.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPassword("Password@123");
        userDTO.setOtp(123456);
        doNothing().when(userService).register(any(UserDTO.class), any(MultipartFile.class));

        MockMultipartFile file = new MockMultipartFile("image", "profile.jpg", "image/jpeg", "dummy content".getBytes());

        mockMvc.perform(multipart("/users")
                        .file(file)
                        .param("email", userDTO.getEmail())
                        .param("firstName", userDTO.getFirstName())
                        .param("lastName", userDTO.getLastName())
                        .param("password", userDTO.getPassword())
                        .param("otp", String.valueOf(userDTO.getOtp())) // Include the OTP parameter
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    void testUserRegistrationInvalidData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "profile.jpg", "image/jpeg", "dummy content".getBytes());

        mockMvc.perform(multipart("/users")
                        .file(file)
                        .param("email", "invalidEmail")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("password", "Password@123")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
                .andExpect(result -> result.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    void testUserRegistrationInvalidFirstName() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "profile.jpg", "image/jpeg", "dummy content".getBytes());

        mockMvc.perform(multipart("/users")
                        .file(file)
                        .param("email", "sample@epam.com")
                        .param("firstName", "")
                        .param("lastName", "Doe")
                        .param("password", "Password@123")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    void testUserRegistrationInvalidLastName() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "profile.jpg", "image/jpeg", "dummy content".getBytes());

        mockMvc.perform(multipart("/users")
                        .file(file)
                        .param("email", "sample@epam.com")
                        .param("firstName", "Jon")
                        .param("lastName", "")
                        .param("password", "Password@123")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    void testUserRegistrationInvalidPassword() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "profile.jpg", "image/jpeg", "dummy content".getBytes());

        mockMvc.perform(multipart("/users")
                        .file(file)
                        .param("email", "sample@epam.com")
                        .param("firstName", "Jon")
                        .param("lastName", "don")
                        .param("password", "normal")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    public void testUserLogin() throws Exception {
        LoginDTO userLoginDto = new LoginDTO();
        userLoginDto.setEmail("abc@gmail.com");
        userLoginDto.setPassword("Password@1234");

        when(userService.login(any(LoginDTO.class))).thenReturn("token");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"developerMessage\":\"token\"}"));

        verify(userService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void testResetPassword() throws Exception {
        doNothing().when(userService).resetPassword(Mockito.<String>any(), Mockito.<String>any());
        ResetPasswordRequestDTO resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setEmail("jane.doe@example.org");
        resetPasswordRequestDTO.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(resetPasswordRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/users/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userAPI)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Password Changed Successfully\"}"));
    }

    @Test
    void validateToken_testReturnsTrue() throws Exception {
        when(tokenService.validateToken(anyString())).thenReturn(true);

        mockMvc.perform(get("/users/validate-token")
                        .param("token","dummy.jwt.token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}