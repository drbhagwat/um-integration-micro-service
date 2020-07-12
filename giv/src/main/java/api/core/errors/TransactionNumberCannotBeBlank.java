package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionNumberCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public TransactionNumberCannotBeBlank(String exception) {
    super(exception);
  }
}
