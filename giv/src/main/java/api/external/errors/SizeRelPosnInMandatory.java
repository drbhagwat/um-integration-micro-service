package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeRelPosnInMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public SizeRelPosnInMandatory(String exception) {
		super(exception);
	}
}
