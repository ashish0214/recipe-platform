package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.request.LoginDTO;
import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.entity.OtpEntity;
import com.epam.recipe.platform.entity.RoleEntity;
import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.exceptionhandler.exception.EmailException;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import com.epam.recipe.platform.repository.OtpRepository;
import com.epam.recipe.platform.repository.RoleRepository;
import com.epam.recipe.platform.repository.UserRepository;
import com.epam.recipe.platform.service.NotificationService;
import com.epam.recipe.platform.service.TokenService;
import com.epam.recipe.platform.service.UserService;
import com.epam.recipe.platform.utils.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.epam.recipe.platform.constants.UserConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final ImageServiceImpl imageService;


    @Override
    @Transactional
    public void register(UserDTO userDTO, MultipartFile file) {
        log.info("Entered into register() service method with request {}", userDTO.toString());
        try {
            UserEntity userEntity = UserMapper.INSTANCE.userDTOToUser(userDTO);
            Optional.ofNullable(file).ifPresent(file1 ->
                    imageService.handleImage(file1, userEntity));
            if (authenticateUserWithOtp(userDTO.getEmail(), userDTO.getOtp())) {
                userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userEntity.setRoles(Set.of(getRole()));
                userRepository.save(userEntity);
                otpRepository.deleteByEmail(userDTO.getEmail());
                log.info("User Details Saved Successfully for email: {}", userDTO.getEmail());
                notificationService.sendNotification(userDTO.getEmail(), MAIL_CONTENT + userDTO.getFirstName(), MAIL_CONTENT);
            }
            else{
                throw new OtpException("Otp is Expired", HttpStatus.GONE);
            }
        } catch (OtpException exception){
            log.error("Invalid OTP provided for email: {}", userDTO.getEmail());
            throw exception;
        }
    }

    public RoleEntity getRole() {
        return roleRepository.findByRoleName(ROLE_USER).orElseGet(() -> {
            RoleEntity newRole = new RoleEntity();
            newRole.setRoleName(ROLE_USER);
            return roleRepository.save(newRole);
        });
    }

    public boolean authenticateUserWithOtp(String email, Integer otp) throws OtpException {
        log.info("Attempting to verify OTP for email: {}", email);
        checkEmailExist(email);
        return Optional.ofNullable(getOtp(email,otp))
                .map(otpentity -> otpentity.getExpirationTime().after(Date.from(Instant.now())))
                .orElseThrow(() -> new OtpException("OTP Not Found.", HttpStatus.NOT_FOUND));
    }

    private OtpEntity getOtp(String email, Integer otp) {
        return otpRepository.findByEmailAndOtp(email,otp);
    }

    private void checkEmailExist(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            log.error("User already exists: {}", email);
                throw new UserException("User already exists.", HttpStatus.CONFLICT);
        });
    }

    @Override
    public String login(@Valid LoginDTO loginDTO) {
        log.info("User login request received: {}",loginDTO);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(), loginDTO.getPassword())
        );
        log.info("User details authenticated successfully");
        return tokenService.generateToken(authentication);
    }

    @Override
    @Transactional
    public void resetPassword(String password, String email) {
        log.info("Resetting password for email: {}", email);
        UserEntity userEntity = findUserByEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        log.info("Password reset successfully for email: {}", email);
        otpRepository.deleteByEmail(email);
        log.info("otp record deleted for email: {}", email);
    }

    public UserEntity findUserByEmail(String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException("Email not found",HttpStatus.NOT_FOUND));
    }
}
