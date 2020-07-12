package com.s3groupinc.gateway.api.repo;

import com.s3groupinc.gateway.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This represents repository for User entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findOneByUsername(String username);

    Optional<User> findByUsernameAndEmail(String userName, String email);

    Optional<User> findOneByEmail(String email);
}