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
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUserName(userName);
    user.orElseThrow(() -> new UsernameNotFoundException("Not Found" + userName));
    return user.map(CustomUserDetails::new).get();
  }
}
