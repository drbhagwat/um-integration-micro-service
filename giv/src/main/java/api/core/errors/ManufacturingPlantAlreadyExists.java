package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public ManufacturingPlantAlreadyExists(String exception) {
    super(exception);
  }
}
