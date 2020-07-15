package com.whizspider.gateway.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomUserDetails implements UserDetails {
  private String userName;
  private String password;
  private boolean active;
  private List<GrantedAuthority> authorities = new ArrayList<>();

  public CustomUserDetails(User user) {
    this.userName = user.getName();
    this.password = user.getPassword();
    this.active = user.isActive();

    List<Role> roles = user.getRoles();
    roles.forEach(element -> this.authorities.add(new SimpleGrantedAuthority(element.getName())));
  }

  @Override
  public String getUsername() {
    return userName;
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
