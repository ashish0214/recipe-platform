package com.epam.recipe.platform.repository;

import com.epam.recipe.platform.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity,Long> {
    OtpEntity findByEmailAndOtp(String email, Integer otp);
    void deleteByEmail(String email);
    Optional<OtpEntity> findByEmail(String email);
}
