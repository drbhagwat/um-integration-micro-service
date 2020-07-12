package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StyleMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public StyleMandatory(String exception) {
		super(exception);
	}
}
