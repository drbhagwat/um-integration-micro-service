package com.whizspider.gateway.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  private String name;

  private String password;

  private boolean active;

  @OneToMany(fetch = FetchType.EAGER,
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<Role> roles = new ArrayList<>();
}
