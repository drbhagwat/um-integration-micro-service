package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public EmailCannotBeBlank(String exception) {
    super(exception);
  }
}