package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyCodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public CompanyCodeMandatory(String exception) {
    super(exception);
  }
}
