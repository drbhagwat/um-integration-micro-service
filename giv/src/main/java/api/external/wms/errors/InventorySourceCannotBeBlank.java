package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventorySourceCannotBeBlank extends Exception {
	public InventorySourceCannotBeBlank(String exception) {
		super(exception);
	}
}