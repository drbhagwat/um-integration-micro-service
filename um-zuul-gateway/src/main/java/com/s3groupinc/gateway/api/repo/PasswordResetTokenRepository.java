package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for PasswordResetToken entity.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2020-02-17
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}