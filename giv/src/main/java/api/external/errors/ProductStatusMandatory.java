package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductStatusMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ProductStatusMandatory(String exception) {
		super(exception);
	}
}
