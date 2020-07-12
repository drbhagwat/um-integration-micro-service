package api.external.mfg.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerOrderNumberCannotBeBlank extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public CustomerOrderNumberCannotBeBlank(String exception) {
		super(exception);
	}
}