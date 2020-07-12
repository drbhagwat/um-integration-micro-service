package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordMatches extends Exception {
	private static final long serialVersionUID = 1L;
	public PasswordMatches(String exception) {
    super(exception);
  }
}