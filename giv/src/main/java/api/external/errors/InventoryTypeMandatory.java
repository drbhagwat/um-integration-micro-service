package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryTypeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public InventoryTypeMandatory(String exception) {
		super(exception);
	}
}
