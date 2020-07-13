package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNameCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public UserNameCannotBeBlank(String exception) {
    super(exception);
  }
}