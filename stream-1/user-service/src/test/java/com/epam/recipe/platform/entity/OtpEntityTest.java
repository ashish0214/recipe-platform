package com.epam.recipe.platform.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OtpEntityTest {

    private OtpEntity otpEntity;

    @BeforeEach
    public void setUp() {
        otpEntity = new OtpEntity();
    }

    @Test
    public void testGettersAndSetters() {
        otpEntity.setId(1L);
        otpEntity.setEmail("test@example.com");
        otpEntity.setOtp(123456);
        Date expirationTime = new Date();
        otpEntity.setExpirationTime(expirationTime);

        assertEquals(1L, otpEntity.getId());
        assertEquals("test@example.com", otpEntity.getEmail());
        assertEquals(123456, otpEntity.getOtp());
        assertEquals(expirationTime, otpEntity.getExpirationTime());
    }

    @Test
    public void testNoArgsConstructor() {
        OtpEntity entity = new OtpEntity();
        assertNotNull(entity);
    }

    @Test
    public void testAllArgsConstructor() {
        Date expirationTime = new Date();
        OtpEntity entity = new OtpEntity(1L, "test@example.com", 123456, expirationTime);

        assertEquals(1L, entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals(123456, entity.getOtp());
        assertEquals(expirationTime, entity.getExpirationTime());
    }

    @Test
    public void testBuilder() {
        Date expirationTime = new Date();
        OtpEntity entity = OtpEntity.builder()
                .id(1L)
                .email("test@example.com")
                .otp(123456)
                .expirationTime(expirationTime)
                .build();

        assertEquals(1L, entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals(123456, entity.getOtp());
        assertEquals(expirationTime, entity.getExpirationTime());
    }
}

