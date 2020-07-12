package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecDimensionMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public SecDimensionMandatory(String exception) {
		super(exception);
	}
}
