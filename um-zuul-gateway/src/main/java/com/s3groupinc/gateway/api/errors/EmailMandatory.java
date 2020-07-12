package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public EmailMandatory(String exception) {
    super(exception);
  }
}