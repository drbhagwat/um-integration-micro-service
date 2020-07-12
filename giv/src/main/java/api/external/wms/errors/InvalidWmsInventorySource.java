package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidWmsInventorySource extends Exception {
	public InvalidWmsInventorySource(String exception) {
		super(exception);
	}
}