package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDoesNotMatchWithRequestedToken extends Exception {
	private static final long serialVersionUID = 1L;
	public UserDoesNotMatchWithRequestedToken(String exception) {
    super(exception);
  }
}