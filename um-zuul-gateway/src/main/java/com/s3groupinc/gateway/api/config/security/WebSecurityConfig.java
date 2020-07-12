package com.s3groupinc.gateway.api.config.security;

import com.s3groupinc.gateway.api.config.jwtutil.JwtAuthenticationEntryPoint;
import com.s3groupinc.gateway.api.config.jwtutil.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${ADMIN_PATH}")
    private String[] adminPath;

    @Value("${USER_PATH}")
    private String[] userPath;

    @Value("${PERMIT_ALL_PATH}")
    private String[] permitAllPath;

    @Value("${WMS_GROUP}")
    private String wmsGroup;

    @Value("${CAMPAIGN_GROUP}")
    private String campaignGroup;

    @Value("${CHANNEL_GROUP}")
    private String channelGroup;

    @Value("${WMSINVENTORY_PATH}")
    private String wmsInventoryPath;

    @Value("${CAMPAIGN_PATH}")
    private String campaignPath;

    @Value("${CHANNELAPI_PATH}")
    private String channelAPIPath;

    @Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;

    @Value("${ldap.password}")
    private String ldapPrincipalPassword;

    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Value("${ldap.enabled}")
    private String ldapEnabled;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (Boolean.parseBoolean(ldapEnabled)) {
            auth
                    .ldapAuthentication()
                    .contextSource()
                    .url(ldapUrls + ldapBaseDn)
                    .managerDn(ldapSecurityPrincipal)
                    .managerPassword(ldapPrincipalPassword)
                    .and()
                    .userDnPatterns(ldapUserDnPattern);
        } else {
            auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
            auth.authenticationProvider(authenticationProvider());
        }
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers(permitAllPath).permitAll()
                .antMatchers(wmsInventoryPath).hasAuthority(wmsGroup)
                .antMatchers(campaignPath).hasAuthority(campaignGroup)
                .antMatchers(channelAPIPath).hasAuthority(channelGroup).anyRequest().authenticated()
                .and().csrf().disable().headers().frameOptions().disable().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}