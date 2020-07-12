package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public DateCannotBeBlank(String exception) {
		super(exception);
	}
}
