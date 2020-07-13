package com.whizspider.gateway.api.repo;

import com.whizspider.gateway.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUserName(String userName);
}
