package com.epam.recipe.platform.api;

import com.epam.recipe.platform.dto.request.VerifyEmailRequestDTO;
import com.epam.recipe.platform.dto.request.VerifyOtpRequestDTO;
import com.epam.recipe.platform.dto.response.ResponseDTO;
import com.epam.recipe.platform.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("otp")
@RequiredArgsConstructor
@Slf4j
public class OtpApi {

    private final OtpService otpService;

    @PostMapping("validate-email")
    @Operation(summary = "Validate OTP", description = "This endpoint validates the OTP sent to the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP validated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP"),
            @ApiResponse(responseCode = "409", description = "Email with OTP already sent")
    })
    public ResponseEntity<ResponseDTO> sendOtpForEmailVerification(@Valid @RequestBody VerifyEmailRequestDTO verifyEmailDTO) throws SQLIntegrityConstraintViolationException {
        log.info("Verifying email: {}", verifyEmailDTO.getEmail());
        otpService.generateOTP(verifyEmailDTO.getEmail());
        return ResponseEntity.ok(new ResponseDTO("Email sent for Verification!"));
    }

    @Operation(summary = "Verify email and send OTP",
            description = "Verifies if the email is registered and sends an OTP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Email already sent")
    })
    @PostMapping("send-otp")
    public ResponseEntity<ResponseDTO> verifyEmailAndSendOtp(@Valid @RequestBody VerifyEmailRequestDTO request) throws SQLIntegrityConstraintViolationException {
        log.info("Verifying email: {}", request.getEmail());
        otpService.verifyEmailAndSendOtp(request.getEmail());
        log.info("Verification email sent to: {}", request.getEmail());
        return ResponseEntity.ok(new ResponseDTO("Email sent successfully"));
    }

    @Operation(summary = "Verify OTP",
            description = "Verifies the validity of the OTP (valid, invalid, or expired).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP"),
            @ApiResponse(responseCode = "410", description = "OTP expired")
    })
    @GetMapping("verify-otp")
    public ResponseEntity<ResponseDTO> verifyOtp(@Valid VerifyOtpRequestDTO request) throws SQLIntegrityConstraintViolationException {
        log.info("Verifying OTP for email: {}", request.getEmail());
        otpService.verifyOtpForPasswordReset(request.getEmail(), request.getOtp());
        log.info("OTP verified successfully for email: {}", request.getEmail());
        return ResponseEntity.ok(new ResponseDTO("OTP verified successfully"));
    }

    @Operation(summary = "Resend OTP",
            description = "Verifies if the email is registered, checks if OTP is expired or already sent, and then resends OTP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP resent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Email already sent")
    })
    @PostMapping("resend-otp")
    public ResponseEntity<ResponseDTO> resendOtp(@Valid @RequestBody VerifyEmailRequestDTO request) throws SQLIntegrityConstraintViolationException {
        log.info("Resending OTP to email: {}", request.getEmail());
        otpService.resendOtp(request.getEmail());
        log.info("OTP resent to email: {}", request.getEmail());
        return ResponseEntity.ok(new ResponseDTO("OTP resent successfully"));
    }


}
