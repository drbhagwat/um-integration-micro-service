package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuAlreadyExists extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MenuAlreadyExists(String exception) {
    super(exception);
  }
}