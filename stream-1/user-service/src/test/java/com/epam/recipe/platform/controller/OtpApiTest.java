package com.epam.recipe.platform.controller;

import com.epam.recipe.platform.api.OtpApi;
import com.epam.recipe.platform.dto.request.VerifyEmailRequestDTO;
import com.epam.recipe.platform.dto.request.VerifyOtpRequestDTO;
import com.epam.recipe.platform.dto.response.ResponseDTO;
import com.epam.recipe.platform.service.OtpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.sql.SQLIntegrityConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OtpApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private OtpApi otpApi;

    @Test
    public void testVerifyEmail() throws SQLIntegrityConstraintViolationException {
        VerifyEmailRequestDTO verifyEmailDTO = new VerifyEmailRequestDTO();
        verifyEmailDTO.setEmail("test@example.com");
        ResponseEntity<ResponseDTO> response = otpApi.sendOtpForEmailVerification(verifyEmailDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( response.getBody(), new ResponseDTO("Email sent for Verification!"));
        verify(otpService, times(1)).generateOTP(verifyEmailDTO.getEmail());
    }



    @Test
    void verifyEmail_SQLIntegrityConstraintViolationException() throws SQLIntegrityConstraintViolationException {
        VerifyEmailRequestDTO verifyEmailDTO = new VerifyEmailRequestDTO();
        verifyEmailDTO.setEmail("test@example.com");
        doThrow(new SQLIntegrityConstraintViolationException()).when(otpService).generateOTP(anyString());
        try {
            otpApi.sendOtpForEmailVerification(verifyEmailDTO);
        } catch (SQLIntegrityConstraintViolationException e) {
            assertEquals(SQLIntegrityConstraintViolationException.class, e.getClass());
            verify(otpService, times(1)).generateOTP(verifyEmailDTO.getEmail());
        }
    }

    @Test
    void verifyEmail_InvalidEmailFormat() {
        VerifyEmailRequestDTO verifyEmailDTO = new VerifyEmailRequestDTO();
        verifyEmailDTO.setEmail("invalid-email-format");

        try {
            otpApi.sendOtpForEmailVerification(verifyEmailDTO);
        } catch (SQLIntegrityConstraintViolationException ignored) {
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class, e.getClass());
        }
    }

    @Test
    void verifyEmail_OTPAlreadySent() throws SQLIntegrityConstraintViolationException {
        VerifyEmailRequestDTO verifyEmailDTO = new VerifyEmailRequestDTO();
        verifyEmailDTO.setEmail("test@example.com");

        doThrow(new SQLIntegrityConstraintViolationException("Email with OTP already sent")).when(otpService).generateOTP(anyString());
        try {
            otpApi.sendOtpForEmailVerification(verifyEmailDTO);
        } catch (SQLIntegrityConstraintViolationException e) {
            assertEquals("Email with OTP already sent", e.getMessage());
            verify(otpService, times(1)).generateOTP(verifyEmailDTO.getEmail());
        }
    }

    @Test
    void testVerifyEmailAndSendOtp() throws Exception {
        VerifyEmailRequestDTO verifyEmailRequestDTO = new VerifyEmailRequestDTO();
        verifyEmailRequestDTO.setEmail("jane.doe@example.org");
        String content = (new ObjectMapper()).writeValueAsString(verifyEmailRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/otp/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(otpApi).build().perform(requestBuilder);

        actualPerformResult.andExpect(status().is(200));
    }
    @Test
    void testVerifyOtp() throws SQLIntegrityConstraintViolationException {

        OtpService otpService = mock(OtpService.class);
        doNothing().when(otpService).verifyOtpForPasswordReset(Mockito.<String>any(), Mockito.<Integer>any());
        OtpApi otpApi = new OtpApi(otpService);

        VerifyOtpRequestDTO request = new VerifyOtpRequestDTO();
        request.setEmail("jane.doe@example.org");
        request.setOtp(1);

        ResponseEntity<ResponseDTO> actualVerifyOtpResult = otpApi.verifyOtp(request);

        verify(otpService).verifyOtpForPasswordReset(eq("jane.doe@example.org"), eq(1));
        assertEquals("OTP verified successfully", actualVerifyOtpResult.getBody().developerMessage());
        assertEquals(200, actualVerifyOtpResult.getStatusCodeValue());
        assertTrue(actualVerifyOtpResult.hasBody());
        assertTrue(actualVerifyOtpResult.getHeaders().isEmpty());
    }

    @Test
    void testResendOtp() throws Exception {
        // Arrange
        VerifyEmailRequestDTO verifyEmailRequestDTO = new VerifyEmailRequestDTO();
        verifyEmailRequestDTO.setEmail("jane.doe@example.org");
        String content = (new ObjectMapper()).writeValueAsString(verifyEmailRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/otp/resend-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(otpApi).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }
}