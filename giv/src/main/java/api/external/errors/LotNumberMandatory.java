package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LotNumberMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public LotNumberMandatory(String exception) {
		super(exception);
	}
}