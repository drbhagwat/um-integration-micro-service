package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DivisionMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public DivisionMaxLengthExceeded(String exception) {
    super(exception);
  }
}