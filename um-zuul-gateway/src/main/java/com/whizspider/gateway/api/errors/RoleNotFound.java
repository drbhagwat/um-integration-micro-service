package com.whizspider.gateway.api.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class RoleNotFound extends Exception {
  private static final long serialVersionUID = 1L;
  public RoleNotFound(String exception) {
    super(exception);
  }
}