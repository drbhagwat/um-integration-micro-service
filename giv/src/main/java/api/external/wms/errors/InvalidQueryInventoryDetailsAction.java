package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidQueryInventoryDetailsAction extends Exception {
	public InvalidQueryInventoryDetailsAction(String exception) {
		super(exception);
	}
}