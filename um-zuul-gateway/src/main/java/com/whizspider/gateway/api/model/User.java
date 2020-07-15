package com.whizspider.gateway.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
  @Id
  private String name;

  private String password;

  private boolean active;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_role",
      joinColumns = @JoinColumn(
          name = "users_name", referencedColumnName = "name"),
      inverseJoinColumns = @JoinColumn(
          name = "role_name", referencedColumnName = "name"))
  private List<Role> roles = new ArrayList<>();
}
