package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StyleSfxMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public StyleSfxMandatory(String exception) {
		super(exception);
	}
}
