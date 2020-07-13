package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FullNameMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public FullNameMandatory(String exception) {
    super(exception);
  }
}