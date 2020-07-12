package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColorMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ColorMandatory(String exception) {
		super(exception);
	}
}
