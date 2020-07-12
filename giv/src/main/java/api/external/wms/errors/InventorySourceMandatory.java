package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventorySourceMandatory extends Exception {
	public InventorySourceMandatory(String exception) {
		super(exception);
	}
}