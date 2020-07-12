package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NameCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public NameCannotBeBlank(String exception) {
    super(exception);
  }
}
