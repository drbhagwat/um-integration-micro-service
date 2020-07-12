package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantCodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public ManufacturingPlantCodeCannotContainSpecialCharacters(String exception) {
    super(exception);
  }
}
