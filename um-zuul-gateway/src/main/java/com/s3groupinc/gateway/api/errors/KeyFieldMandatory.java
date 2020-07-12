package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyFieldMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public KeyFieldMandatory(String exception) {
    super(exception);
  }
}