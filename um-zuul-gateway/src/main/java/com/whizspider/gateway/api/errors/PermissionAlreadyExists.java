package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public PermissionAlreadyExists(String exception) {
    super(exception);
  }
}