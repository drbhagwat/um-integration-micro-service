package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuBarcodeCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public SkuBarcodeCannotBeBlank(String exception) {
		super(exception);
	}
}
