package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidRequestFor extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidRequestFor(String exception) {
		super(exception);
	}
}