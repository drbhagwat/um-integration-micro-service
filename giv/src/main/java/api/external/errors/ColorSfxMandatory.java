package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColorSfxMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ColorSfxMandatory(String exception) {
		super(exception);
	}
}
