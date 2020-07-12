package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryOfOriginMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public CountryOfOriginMandatory(String exception) {
		super(exception);
	}
}
