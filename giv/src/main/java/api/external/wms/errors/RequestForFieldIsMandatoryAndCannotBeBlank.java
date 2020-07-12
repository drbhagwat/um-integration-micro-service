package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestForFieldIsMandatoryAndCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public RequestForFieldIsMandatoryAndCannotBeBlank(String exception) {
		super(exception);
	}
}