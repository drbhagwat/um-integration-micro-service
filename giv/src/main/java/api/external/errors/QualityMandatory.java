package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QualityMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public QualityMandatory(String exception) {
		super(exception);
	}
}
