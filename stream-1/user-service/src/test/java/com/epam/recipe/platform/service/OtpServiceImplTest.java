package com.epam.recipe.platform.service;

import com.epam.recipe.platform.entity.OtpEntity;
import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.exceptionhandler.exception.EmailException;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import com.epam.recipe.platform.repository.OtpRepository;
import com.epam.recipe.platform.repository.UserRepository;
import com.epam.recipe.platform.service.impl.NotificationServiceImpl;
import com.epam.recipe.platform.service.impl.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private OtpServiceImpl otpService;

    private final String email = "test@example.com";
    private final Integer otp = 123456;
    private OtpEntity otpEntity;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        otpEntity = OtpEntity.builder()
                .email(email)
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000))
                .build();

        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(email);
        user.setPassword("password");
    }

    @Test
    public void testGenerateOTP_EmailAlreadyExists() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        EmailException exception = assertThrows(EmailException.class, () -> otpService.generateOTP(email));
        assertEquals("Email already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    public void testGenerateOTP_AfterExpirationTime() {
        String email = "test@example.com";
        OtpEntity otpEntity = new OtpEntity();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otpEntity));
        assertDoesNotThrow(() -> otpService.generateOTP(email));
    }

    @Test
    public void testVerifyEmailAndSendOtp_EmailExists() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
        assertDoesNotThrow(() -> otpService.verifyEmailAndSendOtp(email));
        verify(userRepository, times(1)).findByEmail(email);
        verify(otpRepository, times(1)).save(any());
    }

    @Test
    public void testVerifyEmailAndSendOtp_EmailDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EmailException.class, () -> otpService.verifyEmailAndSendOtp(email));
    }

    @Test
    public void testVerifyOtpForPasswordReset_Success() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(otpRepository.findByEmailAndOtp(email, otp)).thenReturn((otpEntity));
        assertDoesNotThrow(() -> otpService.verifyOtpForPasswordReset(email, otp));
    }

    @Test
    public void testVerifyOtpForPasswordReset_EmailNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailException exception = assertThrows(EmailException.class, () -> otpService.verifyOtpForPasswordReset(email, otp));
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    public void testVerifyOtpForPasswordReset_OtpExpired(){
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setExpirationTime(Date.from(Instant.now().minusSeconds(1))); // OTP expired 1 second ago
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(otpRepository.findByEmailAndOtp(email, otp)).thenReturn(otpEntity);
        OtpException exception = assertThrows(OtpException.class, () -> otpService.verifyOtpForPasswordReset(email, otp));
        assertEquals("OTP Expired", exception.getMessage());
    }

    @Test
    public void testResendOtp_EmailNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailException exception = assertThrows(EmailException.class, () -> otpService.resendOtp(email));
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    public void testResendOtp_NoOtpFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(otpRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailException exception = assertThrows(EmailException.class, () -> otpService.resendOtp(email));
        assertEquals("No OTP found for email", exception.getMessage());
    }

    @Test
    public void testGenerateOTPSuccess() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        otpService.generateOTP(email);
        verify(otpRepository, times(1)).save(any(OtpEntity.class));
        verify(notificationService, times(1)).sendNotification(eq(email), anyString(), anyString());
    }

    @Test
    public void testResendOtp_EmailExists_OtpExists_OtpExpired() {
        String email = "test@example.com";
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setExpirationTime(Date.from(Instant.now().minusSeconds(1))); // OTP expired 1 second ago
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otpEntity));
        assertDoesNotThrow(() -> otpService.resendOtp(email));
        verify(userRepository, times(1)).findByEmail(email);
        verify(otpRepository, times(1)).findByEmail(email);
        verify(otpRepository, times(1)).save(any());
        verify(notificationService, times(1)).sendNotification(any(), any(), any());
    }

    @Test
    public void testResendOtp_EmailDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EmailException.class, () -> otpService.resendOtp(email));
    }

    @Test
    public void testResendOtp_EmailExists_OtpDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
        when(otpRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EmailException.class, () -> otpService.resendOtp(email));
    }

    @Test
    public void testResendOtp_EmailExists_OtpExists_OtpNotExpired() {
        String email = "test@example.com";
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setExpirationTime(Date.from(Instant.now().plusSeconds(60)));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
        when(otpRepository.findByEmail(email)).thenReturn(Optional.of(otpEntity));
        assertThrows(EmailException.class, () -> otpService.resendOtp(email));
    }
}