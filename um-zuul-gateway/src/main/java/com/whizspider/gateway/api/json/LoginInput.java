package com.whizspider.gateway.api.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class LoginInput {
  // indicates the user name
  private String userName;

  // indicates the password
  private String password;
}
