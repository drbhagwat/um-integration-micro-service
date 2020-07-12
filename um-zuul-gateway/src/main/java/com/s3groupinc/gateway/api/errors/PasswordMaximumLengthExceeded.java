package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordMaximumLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public PasswordMaximumLengthExceeded(String exception) {
    super(exception);
  }
}