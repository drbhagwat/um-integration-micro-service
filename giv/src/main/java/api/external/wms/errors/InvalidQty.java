package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidQty extends Exception {
	public InvalidQty(String exception) {
		super(exception);
	}
}