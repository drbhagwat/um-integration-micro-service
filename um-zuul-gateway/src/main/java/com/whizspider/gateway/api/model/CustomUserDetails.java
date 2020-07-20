package com.whizspider.gateway.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class CustomUserDetails implements UserDetails {
  private String name;
  private String password;
  private boolean active;
  private List<GrantedAuthority> authorities = new ArrayList<>();

  public CustomUserDetails(User user) {
    this.name = user.getName();
    this.password = user.getPassword();
    this.active = user.isActive();

    for (Role role : user.getRoles()) {
      this.authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }
}
