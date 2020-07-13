package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminNameMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public AdminNameMandatory(String exception) {
    super(exception);
  }
}