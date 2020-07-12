package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyFieldCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public KeyFieldCannotBeBlank(String exception) {
    super(exception);
  }
}
