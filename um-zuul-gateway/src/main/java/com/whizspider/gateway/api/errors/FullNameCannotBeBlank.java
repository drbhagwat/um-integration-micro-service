package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FullNameCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public FullNameCannotBeBlank(String exception) {
    super(exception);
  }
}