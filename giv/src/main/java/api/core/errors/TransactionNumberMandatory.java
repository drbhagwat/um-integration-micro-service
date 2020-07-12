package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionNumberMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public TransactionNumberMandatory(String exception) {
    super(exception);
  }
}
