package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DivisionNotFoundInCompany extends Exception {
	private static final long serialVersionUID = 1L;
	public DivisionNotFoundInCompany(String exception) {
		super(exception);
	}
}
