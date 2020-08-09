package com.whizspider.gateway.api.config.jpa;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Puts auditing information (i.e., the currently logged in user as the CreatedUser and/or Updated User. Also puts the current system date & time as the Created DateTime and/or Updated DateTime for all db records.
 */

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    return Optional.of(username);
  }
}