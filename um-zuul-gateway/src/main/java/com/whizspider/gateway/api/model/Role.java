package com.whizspider.gateway.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
  @Id
  private String name;

  @ManyToOne
  @JoinColumn(name = "users_name", nullable = false)
  private User user;
}
