package com.whizspider.gateway.api.config.jpa;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Puts auditing information (i.e., the currently logged in giv user as the Created User and/or Updated User.
 * Also puts the current system date & time as the Created DateTime and/or Updated DateTime for all db records.
 */

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;

    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    return Optional.of(username);
  }
}