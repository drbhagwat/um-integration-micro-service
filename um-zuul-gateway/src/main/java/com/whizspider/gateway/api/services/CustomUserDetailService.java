package com.whizspider.gateway.api.services;

import com.whizspider.gateway.api.model.CustomUserDetails;
import com.whizspider.gateway.api.model.User;
import com.whizspider.gateway.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByName(name);
    user.orElseThrow(() -> new UsernameNotFoundException("Not Found" + name));
    return new CustomUserDetails(user.get());
  }
}
