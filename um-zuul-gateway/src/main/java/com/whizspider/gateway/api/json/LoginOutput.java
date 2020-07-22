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
public class LoginOutput {
  /*
   * true only when the login becomes successful for the first time
   * afterwards this is untouched; The reason for this flag is to
   * is to force the change password.
   */
  boolean firstSuccessfulLogin;

  // indicates if the most recent login attempt was successful or not
  boolean isMostRecentLoginSuccessful;

  // this carries the precise message about the outcome of the login
  String message;
}
