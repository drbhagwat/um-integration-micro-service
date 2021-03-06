package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DivisionCodeCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public DivisionCodeCannotBeBlank(String exception) {
    super(exception);
  }
}
