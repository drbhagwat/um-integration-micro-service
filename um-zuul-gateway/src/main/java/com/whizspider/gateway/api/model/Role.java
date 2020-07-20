package com.whizspider.gateway.api.model;

import com.whizspider.gateway.api.config.security.BasicLogger;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class Role extends BasicLogger<String> {
  @Id
  private String name;

/*
  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  private Set<User> users = new HashSet<>();
*/
}
