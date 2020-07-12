package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenExpired extends Exception {
	private static final long serialVersionUID = 1L;
	public TokenExpired(String exception) {
    super(exception);
  }
}