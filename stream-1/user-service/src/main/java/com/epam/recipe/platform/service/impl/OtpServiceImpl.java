package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.entity.OtpEntity;
import com.epam.recipe.platform.exceptionhandler.exception.EmailException;
import com.epam.recipe.platform.exceptionhandler.exception.OtpException;
import com.epam.recipe.platform.repository.OtpRepository;
import com.epam.recipe.platform.repository.UserRepository;
import com.epam.recipe.platform.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final NotificationServiceImpl notificationService;

    @Override
    public void generateOTP(final String email) {
        log.info("Generating OTP for email: {}", email);
        validateEmailNotExist(email);
        OtpEntity otpEntity = isEmailExist(email)
                ? updateExistingOtpEntity(email)
                : createNewOtpEntity(email);
        saveOtpAndNotify(email, otpEntity.getOtp(), otpEntity);
    }

    private OtpEntity createNewOtpEntity(String email) {
        return createOtpEntity(email, generateRandomOtp(), calculateExpirationTime());
    }

    private OtpEntity updateExistingOtpEntity(String email) {
        OtpEntity otpEntity = otpRepository.findByEmail(email).get();
        otpEntity.setOtp(generateRandomOtp());
        otpEntity.setExpirationTime(calculateExpirationTime());
        return otpEntity;
    }

    private void validateEmailNotExist(final String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new EmailException("Email already exists", HttpStatus.CONFLICT);
        });
    }

    private boolean isEmailExist(String email){
        return otpRepository.findByEmail(email).isPresent();
    }

    private Integer generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        log.info("OTP generated successfully: {}", otp);
        return otp;
    }

    private Date calculateExpirationTime(){
        return new Date(System.currentTimeMillis() + 5*60 * 1000);
    }

    private OtpEntity createOtpEntity(final String email, final Integer otp, final Date expirationTime) {
        return OtpEntity.builder()
                .otp(otp)
                .expirationTime(expirationTime)
                .email(email)
                .build();
    }

    private void saveOtpAndNotify(final String email, final Integer otp, final OtpEntity otpEntity) {
            otpRepository.save(otpEntity);
        notificationService.sendNotification(email, "OTP request received", "Your OTP is: " + otp);
    }

    @Override
    public void verifyEmailAndSendOtp(String email) {
        log.info("Verifying email and sending OTP: {}", email);
        validateEmailExists(email);
        Integer otp = generateRandomOtp();
        Date expirationTime = calculateExpirationTime();
        OtpEntity otpEntity = createOtpEntity(email, otp, expirationTime);
        saveOtpAndNotify(email, otp, otpEntity);
    }

    private void validateEmailExists(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException("Email not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public void verifyOtpForPasswordReset(String email, Integer otp) {
        log.info("Verifying OTP for password reset for email: {}", email);
        validateEmailExists(email);
        OtpEntity otpEntity = otpRepository.findByEmailAndOtp(email, otp);
        validateOtpNotExpired(otpEntity);
        log.info("OTP verified successfully for email: {}", email);
    }

    @Override
    public void resendOtp(String email) {
        log.info("Resending OTP for email: {}", email);
        validateEmailExists(email);
        OtpEntity otpEntity = otpRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException("No OTP found for email" ,HttpStatus.NOT_FOUND));
        validateOtpExpired(otpEntity);
        Integer newOtp = generateRandomOtp();
        log.info("New OTP generated: {}", newOtp);
        otpEntity.setOtp(newOtp);
        otpEntity.setExpirationTime(new Date(System.currentTimeMillis() + 5*60 * 1000));
        otpRepository.save(otpEntity);
        log.info("OTP updated successfully for email: {}", email);
        notifyOtpResent(email, newOtp);
        log.info("mail sent to email: {}", email);
    }

    private void validateOtpExpired(OtpEntity otpEntity) {
        if (otpEntity.getExpirationTime().after(Date.from(Instant.now()))) {
            log.error("OTP already sent and not expired for email: {}", otpEntity.getEmail());
            throw new EmailException("Email already sent", HttpStatus.CONFLICT);
        }
    }

    private void validateOtpNotExpired(OtpEntity otpEntity) {
        if (otpEntity.getExpirationTime().before(Date.from(Instant.now()))) {
            log.info("OTP expired for email: {}", otpEntity.getEmail());
            throw new OtpException("OTP Expired", HttpStatus.GONE);
        }
    }

    private void notifyOtpResent(String email, Integer newOtp) {
        notificationService.sendNotification(email, "OTP resent", "Your new OTP is: " + newOtp);
        log.info("New OTP sent and saved for email: {}", email);
    }
}
