package com.epam.recipe.platform.service;

import java.sql.SQLIntegrityConstraintViolationException;

public interface OtpService {
    void generateOTP(String email) throws SQLIntegrityConstraintViolationException;
    void verifyEmailAndSendOtp(String email) throws SQLIntegrityConstraintViolationException;
    void verifyOtpForPasswordReset(String email, Integer otp) throws SQLIntegrityConstraintViolationException;
    void resendOtp(String email) throws SQLIntegrityConstraintViolationException;
}
