package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuBarcodeMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public SkuBarcodeMaxLengthExceeded(String exception) {
    super(exception);
  }
}