package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateTimeIsMandatoryAndCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public DateTimeIsMandatoryAndCannotBeBlank(String exception) {
		super(exception);
	}
}
