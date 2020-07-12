package com.s3groupinc.gateway.api.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileStorageException extends Exception {
	private static final long serialVersionUID = 1L;
    public FileStorageException(String exception) {
      super(exception);
    }
}