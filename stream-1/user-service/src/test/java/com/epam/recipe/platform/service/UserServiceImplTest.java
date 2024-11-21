package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.request.LoginDTO;
import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.entity.OtpEntity;
import com.epam.recipe.platform.entity.RoleEntity;
import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.exceptionhandler.exception.*;
import com.epam.recipe.platform.repository.OtpRepository;
import com.epam.recipe.platform.repository.RoleRepository;
import com.epam.recipe.platform.repository.UserRepository;
import com.epam.recipe.platform.service.impl.ImageServiceImpl;
import com.epam.recipe.platform.service.impl.UserServiceImpl;
import com.epam.recipe.platform.utils.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import static com.epam.recipe.platform.constants.UserConstant.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ImageServiceImpl imageService;

    @Test
    public void testRegisterUserSuccess() {
        UserDTO userDTO = createUserDTO();
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        RoleEntity roleEntity = createRoleEntity();
        UserEntity mockUserEntity = createUserEntity(userDTO);
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(otpRepository.findByEmailAndOtp(userDTO.getEmail(), userDTO.getOtp())).thenReturn(createOtpEntity());
        when(roleRepository.findByRoleName(roleEntity.getRoleName())).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserEntity);
        userService.register(userDTO, file);
        verify(userRepository, times(1)).save(userEntityCaptor.capture());
        UserEntity userEntityArg = userEntityCaptor.getValue();
        assertEquals(userDTO.getEmail(), userEntityArg.getEmail());
        assertEquals(userDTO.getFirstName(), userEntityArg.getFirstName());
        verify(notificationService, times(1)).sendNotification(eq(userDTO.getEmail()), anyString(), anyString());
    }

    @Test
    void testRegisterUser_ExpiredOtp() {
        UserDTO userDTO = createUserDTO();
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        OtpEntity expiredOtpEntity = createOtpEntity_WithExpiredOtpTime();
        when(otpRepository.findByEmailAndOtp(userDTO.getEmail(), userDTO.getOtp())).thenReturn(expiredOtpEntity);

        OtpException exception = assertThrows(OtpException.class,
                () -> userService.register(userDTO, file),
                "Expected register() to throw OtpException");
        assertEquals("Otp is Expired", exception.getMessage());
    }

    @Test
    void testGetRoleWhenRoleExists() {
        RoleEntity existingRole = new RoleEntity();
        existingRole.setId(1L);
        existingRole.setRoleName("USER");
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(existingRole));
        RoleEntity result = userService.getRole();
        verify(roleRepository, times(1)).findByRoleName("USER");
        assertEquals(existingRole, result);
    }
    @Test
    void testGetRoleWhenRoleDoesNotExist() {
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.empty());
        RoleEntity savedRole = new RoleEntity();
        savedRole.setId(1L);
        savedRole.setRoleName("USER");
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(savedRole);
        RoleEntity result = userService.getRole();
        verify(roleRepository, times(1)).findByRoleName("USER");
        verify(roleRepository, times(1)).save(any(RoleEntity.class));
        assertEquals(savedRole, result);
    }

    @Test
    public void testProcessImage_NullFile_NoImageToken() {
        UserDTO userDTO = createUserDTO();
        UserEntity userEntity = createUserEntity(userDTO);
        imageService.handleImage(null, userEntity);
        assertNull(userEntity.getImageUrl());
    }

    @Test
    public void testGetOrCreateUserRole_RoleExists() {
        RoleEntity existingRole = new RoleEntity();
        existingRole.setRoleName("ROLE_USER");
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(existingRole));
        RoleEntity result = userService.getRole();
        assertEquals(existingRole, result, "Expected existing role");
        verify(roleRepository, times(1)).findByRoleName(anyString());
        verify(roleRepository, times(0)).save(any(RoleEntity.class));
    }

    @Test
    public void testVerifyUser_UserAlreadyExists() {
        String email = "test@example.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        assertThrows(UserException.class, () -> userService.authenticateUserWithOtp(email, 123456), "Expected UserException");
    }

    @Test
    public void testVerifyUser_OtpExpired() {
        String email = "test@example.com";
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setExpirationTime(Date.from(Instant.now().minusSeconds(190))); // OTP expired 1 second ago
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(OtpException.class, () -> userService.authenticateUserWithOtp(email, 123456), "Expected EmailException");
    }

    @Test
    void register_OtpNotAuthenticated_Invalid_OTP_provided() {
        UserDTO testUserDTO = createUserDTO();
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        when(otpRepository.findByEmailAndOtp(testUserDTO.getEmail(), testUserDTO.getOtp())).thenReturn(createOtpEntity_WithExpiredOtpTime());
        when(userService.authenticateUserWithOtp(testUserDTO.getEmail(), testUserDTO.getOtp())).thenThrow(new OtpException("Invalid OTP provided", HttpStatus.GONE));
        OtpException exception = assertThrows(OtpException.class,
                () -> userService.register(testUserDTO, file),
                "Expected register() to throw OtpException");
        assertTrue(exception.getMessage().contains("Invalid OTP provided"));
    }

    @Test
    void register_OtpNotAuthenticated_OTP_Expired(){
        UserDTO testUserDTO = createUserDTO();
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(testUserDTO.getEmail());
        otpEntity.setOtp(testUserDTO.getOtp());
        otpEntity.setExpirationTime(Date.from(Instant.now().minusSeconds(1)));
        MultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(otpRepository.findByEmailAndOtp(eq(testUserDTO.getEmail()), eq(testUserDTO.getOtp()))).thenReturn(otpEntity);
        OtpException exception = assertThrows(OtpException.class, () -> {
            userService.register(testUserDTO, file);
        });
        assertEquals("Otp is Expired", exception.getMessage());
        assertEquals(HttpStatus.GONE, exception.getStatus());
    }

    @Test
    void testVerifyUser_InvalidOtp() {
        String email = "test@gmail.com";
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(123457);
        otpEntity.setExpirationTime(Date.from(Instant.now().plusSeconds(60)));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(OtpException.class, () -> userService.authenticateUserWithOtp(email, 123456));
    }

    @Test
    void testVerifyUser_OtpNotFound() {
        String email = "test@gmail.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(OtpException.class, () -> userService.authenticateUserWithOtp(email, 123456), "Expected OtpException");
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("testEmail");
        loginDTO.setPassword("testPassword");
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(tokenService.generateToken(auth)).thenReturn("mockToken");

        String token = userService.login(loginDTO);

        assertEquals("mockToken", token);
        verify(tokenService).generateToken(any());
        verify(authenticationManager).authenticate(any());
    }


    @Test
    void shouldFailOnLogin(){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("testEmail");
        loginDTO.setPassword("testPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Failed") {});
        assertThrows(AuthenticationException.class, () -> userService.login(loginDTO));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void shouldThrowOnLogin(){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("testEmail");
        loginDTO.setPassword("testPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.login(loginDTO));
        verify(authenticationManager,times(1)).authenticate(any());
    }

    @Test
    void testResetPassword_UserExists() {
        String email = "test@example.com";
        String password = "password";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        userService.resetPassword(password, email);
        assertEquals("encodedPassword", userEntity.getPassword(), "Expected password to be updated");
        verify(userRepository, times(1)).save(userEntity);
        verify(otpRepository, times(1)).deleteByEmail(email);
    }

    @Test
    public void testResetPassword_UserDoesNotExist() {
        String email = "test@example.com";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EmailException.class, () -> userService.resetPassword(password, email), "Expected EmailException");
    }

    @Test
    public void testFindUserByEmail_UserExists() {
        // Given
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        UserEntity result = userService.findUserByEmail(email);
        assertEquals(userEntity, result, "Expected user to be found");
    }

    @Test
    public void testFindUserByEmail_UserDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EmailException.class, () -> userService.findUserByEmail(email), "Expected EmailException");
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPassword("password");
        userDTO.setOtp(123456);
        return userDTO;
    }

    private UserEntity createUserEntity(UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser(userDTO);
    }

    private OtpEntity createOtpEntity() {
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setId(1L);
        otpEntity.setEmail("test@gmail.com");
        otpEntity.setOtp(123456);
        otpEntity.setExpirationTime(Date.from(Instant.now().plus(Duration.ofHours(1))));
        return otpEntity;
    }

    private RoleEntity createRoleEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setRoleName(ROLE_USER);
        return roleEntity;
    }

    private OtpEntity createOtpEntity_WithExpiredOtpTime() {
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setId(1L);
        otpEntity.setEmail("test@gmail.com");
        otpEntity.setOtp(123456);
        otpEntity.setExpirationTime(Date.from(Instant.now().minus(Duration.ofHours(2))));
        return otpEntity;
    }
}