package com.s3groupinc.gateway.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This class represents um user and implements spring's userDetails
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Entity
@Cacheable
@Table(name = "users")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotNull(message = "{USERNAME_MANDATORY}")
    @NotBlank(message = "{USERNAME_CANNOT_BE_BLANK}")
    @Column(unique = true)
    private String username;

    @NotNull(message = "{FULLNAME_MANDATORY}")
    @NotBlank(message = "{FULLNAME_CANNOT_BE_BLANK}")
    private String fullName;

    @NotNull(message = "{EMAIL_MANDATORY}")
    @NotBlank(message = "{EMAIL_CANNOT_BE_BLANK}")
    @Email
    @Column(unique = true)
    private String email;

    private String password;

    private String newPassword = "";

    // the flag below indicates if the user is currently active or not.
    @Column(columnDefinition = "BOOLEAN DEFAULT false", name = "isActive")
    private boolean active;

    // the flag below indicates if the user is admin or not
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isAdmin;

    @NotNull
    private boolean enabled = true;

    @NotNull(message = "{API_ENABLED_SHOULD_BE_TRUE_OR_FALSE}")
    private boolean apiEnabled;

    @NotNull
    private boolean firstLogin;

    @Column(name = "account_locked")
    private boolean accountNonLocked = false;

    @Column(name = "account_expired")
    private boolean accountNonExpired = false;

    @Column(name = "credentials_expired")
    private boolean credentialsNonExpired = false;

    private LocalDate expiredDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usersId")
    private List<Role> roles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "usersId")
    private List<Groups> groups = new ArrayList<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        roles.forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r.getName().toUpperCase())));
        //groups.forEach(g -> g.getPermissionsList().forEach(p -> grantedAuthorities.add(new SimpleGrantedAuthority(p.getPermissionName()))));
        return grantedAuthorities;
    }

    public boolean isAccountNonExpired() {
        return !accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return !accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return !credentialsNonExpired;
    }
}