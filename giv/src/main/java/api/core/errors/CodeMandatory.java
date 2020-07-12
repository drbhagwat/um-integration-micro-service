package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public CodeMandatory(String exception) {
    super(exception);
  }
}
