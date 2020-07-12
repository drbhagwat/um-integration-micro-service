package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MenuNotFound(String exception) {
    super(exception);
  }
}