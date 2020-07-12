package api.external.mfg.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerOrderNumberMandatory extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public CustomerOrderNumberMandatory(String exception) {
		super(exception);
	}
}