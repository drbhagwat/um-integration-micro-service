package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidInventoryType extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidInventoryType(String exception) {
		super(exception);
	}
}
