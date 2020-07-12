package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuBarcodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public SkuBarcodeCannotContainSpecialCharacters(String exception) {
		super(exception);
	}
}
