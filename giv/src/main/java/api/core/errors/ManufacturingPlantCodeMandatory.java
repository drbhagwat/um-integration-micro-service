package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantCodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ManufacturingPlantCodeMandatory(String exception) {
    super(exception);
  }
}
