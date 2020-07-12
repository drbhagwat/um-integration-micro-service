package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeRngeCodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public SizeRngeCodeMandatory(String exception) {
		super(exception);
	}
}
