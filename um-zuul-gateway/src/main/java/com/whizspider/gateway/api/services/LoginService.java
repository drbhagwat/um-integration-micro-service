package com.whizspider.gateway.api.services;

import com.whizspider.gateway.api.errors.UserNotFound;
import com.whizspider.gateway.api.json.LoginInput;
import com.whizspider.gateway.api.json.LoginOutput;
import com.whizspider.gateway.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginService {
  @Autowired
  private UserService userService;

  @Autowired
  private LoginOutput loginOutput;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public LoginOutput login(LoginInput loginInput) {
    String userName = loginInput.getUserName();

    try {
      User user = userService.get(userName);
      String password = loginInput.getPassword();
      boolean match = passwordEncoder.matches(password, user.getPassword());

      if (!match) {
        loginOutput.setMessage("The password for " + userName + " is not the same as the one stored in the system. Please retry with the correct password.");
        loginOutput.setFirstSuccessfulLogin(false);
        loginOutput.setMostRecentLoginSuccessful(false);
      } else {
        user.setFirstSuccessfulLogin(true);
        user.setMostRecentLoginSuccessful(true);
        userService.update(userName, user);
        loginOutput.setMessage("Welcome " + userName + ". You are logged in.");
        loginOutput.setFirstSuccessfulLogin(true);
        loginOutput.setMostRecentLoginSuccessful(true);
      }
    } catch (UserNotFound userNotFound) {
      loginOutput.setMessage("The user name " + userName + " is not found in the system");
      loginOutput.setFirstSuccessfulLogin(false);
      loginOutput.setMostRecentLoginSuccessful(false);
    } finally {
      return loginOutput;
    }
  }
}
