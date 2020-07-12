package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuNotFound extends Exception {
	private static final long serialVersionUID = 1L;
	public SkuNotFound(String exception) {
    super(exception);
  }
}