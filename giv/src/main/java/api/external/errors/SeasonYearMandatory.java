package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeasonYearMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public SeasonYearMandatory(String exception) {
		super(exception);
	}
}
