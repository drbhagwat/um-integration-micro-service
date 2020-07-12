package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public PermissionNotFound(String exception) {
    super(exception);
  }
}