package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidPassword extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidPassword(String exception) {
    super(exception);
  }
}