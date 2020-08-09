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
  private static final String FALSE = "false";
  private static final String TRUE = "true";

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
      String userSignedInSuccessfullyBefore = user.getFirstSuccessfulLogin();
      boolean match = passwordEncoder.matches(password, user.getPassword());

      if (!match) { // password does not match

        if (userSignedInSuccessfullyBefore.equals(FALSE)) {
          // first login attempt
          loginOutput.setFirstSuccessfulLogin(FALSE);
          user.setFirstSuccessfulLogin(FALSE);
        } else {
          // second or later login attempt
          loginOutput.setFirstSuccessfulLogin("NA");
          user.setFirstSuccessfulLogin("NA");
        }
        loginOutput.setMessage("The password for " + userName + " is incorrect. Please retry with the correct password.");
        loginOutput.setMostRecentLoginSuccessful(false);
        user.setMostRecentLoginSuccessful(false);
      } else { // password and user name both match
        user.setMostRecentLoginSuccessful(true);

        if (userSignedInSuccessfullyBefore.equals(FALSE)) {
          // first attempt
          loginOutput.setFirstSuccessfulLogin(TRUE);
          user.setFirstSuccessfulLogin(TRUE);
        } else {
          // second or subsequent attempt
          user.setFirstSuccessfulLogin("NA");
          loginOutput.setFirstSuccessfulLogin("NA");
        }
        loginOutput.setMostRecentLoginSuccessful(true);
        loginOutput.setMessage("Welcome " + userName + ". You are logged in.");
      }
      userService.update(userName, user);
    } catch (UserNotFound userNotFound) {
      loginOutput.setFirstSuccessfulLogin(FALSE);
      loginOutput.setMostRecentLoginSuccessful(false);
      loginOutput.setMessage("The user name " + userName + " is not found in the system");
    }
    return loginOutput;
  }
}
