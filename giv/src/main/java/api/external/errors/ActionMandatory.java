package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActionMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ActionMandatory(String exception) {
		super(exception);
	}
}
