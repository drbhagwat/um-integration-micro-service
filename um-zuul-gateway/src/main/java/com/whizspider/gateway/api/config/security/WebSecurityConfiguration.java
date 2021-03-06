package com.whizspider.gateway.api.config.security;

import com.whizspider.gateway.api.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Value("${PERMIT_ALL_PATH}")
  private String[] permitAllPath;

  @Value("${ADMIN_PATH}")
  private String[] adminPath;

  @Value("${USER_PATH}")
  private String[] userPath;

  @Autowired
  private CustomUserDetailService customUserDetailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable().authorizeRequests()
        .antMatchers(adminPath).hasRole("ADMIN")
        .antMatchers(userPath).hasAnyRole("USER", "ADMIN")
        .antMatchers(permitAllPath).permitAll();

/*
        .and().csrf().ignoringAntMatchers("/h2-console/**") //don't apply CSRF protection to /h2-console
        .and().headers().frameOptions().sameOrigin(); //allow use of frame to same origin urls
*/
  }
}
