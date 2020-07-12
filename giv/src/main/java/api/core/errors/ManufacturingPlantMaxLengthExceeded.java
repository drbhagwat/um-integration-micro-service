package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public ManufacturingPlantMaxLengthExceeded(String exception) {
    super(exception);
  }
}