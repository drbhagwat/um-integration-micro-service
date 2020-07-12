package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateIsMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public DateIsMandatory(String exception) {
		super(exception);
	}
}
