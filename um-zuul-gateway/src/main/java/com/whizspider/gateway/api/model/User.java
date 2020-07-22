package com.whizspider.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class User extends CommonFields<String> {
  @Id
  private String name;

  private String password;

  private boolean active;

  private String firstSuccessfulLogin;

  private boolean isMostRecentLoginSuccessful;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  @JoinTable(
      name = "users_role",
      joinColumns = @JoinColumn(
          name = "users_name", referencedColumnName = "name"),
      inverseJoinColumns = @JoinColumn(
          name = "role_name", referencedColumnName = "name"))
  private Set<Role> roles = new HashSet<>();
}
