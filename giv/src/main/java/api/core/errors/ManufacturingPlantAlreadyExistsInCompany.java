package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantAlreadyExistsInCompany extends Exception {
	private static final long serialVersionUID = 1L;
	public ManufacturingPlantAlreadyExistsInCompany(String exception) {
    super(exception);
  }
}
