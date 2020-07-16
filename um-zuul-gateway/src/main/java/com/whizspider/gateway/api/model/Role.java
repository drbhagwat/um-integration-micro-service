package com.whizspider.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whizspider.gateway.api.config.security.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role extends BasicLogger<String> {
  @Id
  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  private List<User> users = new ArrayList<>();
}
