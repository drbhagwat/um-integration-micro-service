package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public RoleAlreadyExists(String exception) {
    super(exception);
  }
}