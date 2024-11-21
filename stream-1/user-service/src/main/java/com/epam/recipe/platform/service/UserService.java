package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.request.LoginDTO;
import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.exceptionhandler.exception.EmailException;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;

public interface UserService {
    String login(@Valid LoginDTO userLoginDetails);
    void register(UserDTO userDTO, MultipartFile file) ;
    boolean authenticateUserWithOtp(String email, Integer otp) ;
    void resetPassword(String password, String email);
}
