package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidPasswordSpace extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidPasswordSpace(String exception) {
    super(exception);
  }
}