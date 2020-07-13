package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GroupNotFound(String exception) {
    super(exception);
  }
}