package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyNotFound extends Exception {
	private static final long serialVersionUID = 1L;
	public CompanyNotFound(String exception) {
    super(exception);
  }
}
