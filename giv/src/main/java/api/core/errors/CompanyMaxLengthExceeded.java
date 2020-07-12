package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public CompanyMaxLengthExceeded(String exception) {
    super(exception);
  }
}