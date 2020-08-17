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
  private static final String NA = "NA";

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

      if (!match) { // passwords do not match

        if (userSignedInSuccessfullyBefore.equals(FALSE)) {
          // this is the very first login attempt
          loginOutput.setFirstSuccessfulLogin(FALSE);
        } else {
          // this is either the second or a subsequent login attempt
          loginOutput.setFirstSuccessfulLogin(NA);
          user.setFirstSuccessfulLogin(NA);
          // finally update the user record
          userService.update(userName, user);
        }
        loginOutput.setMessage("The password for the user " + userName + " is incorrect. Please retry with the correct password.");
        loginOutput.setMostRecentLoginSuccessful(false);
      } else { // password and user name both match

        if (userSignedInSuccessfullyBefore.equals(FALSE)) {
          // this is the very first login attempt
          loginOutput.setFirstSuccessfulLogin(TRUE);
          user.setFirstSuccessfulLogin(TRUE);
        } else {
          // this is either the second or a subsequent login attempt
          loginOutput.setFirstSuccessfulLogin(NA);
          user.setFirstSuccessfulLogin(NA);
        }
        loginOutput.setMostRecentLoginSuccessful(true);
        user.setMostRecentLoginSuccessful(true);
        loginOutput.setMessage("Welcome " + userName + ". You are logged in.");
        // finally update the user record
        userService.update(userName, user);
      }
    } catch (UserNotFound userNotFound) {
      loginOutput.setFirstSuccessfulLogin(FALSE);
      loginOutput.setMostRecentLoginSuccessful(false);
      loginOutput.setMessage("The user name " + userName + " is not found!");
    }
    return loginOutput;
  }
}
