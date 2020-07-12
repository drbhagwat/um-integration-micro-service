package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufacturingPlantNotFoundInCompany extends Exception {
	private static final long serialVersionUID = 1L;
  public ManufacturingPlantNotFoundInCompany(String exception) {
    super(exception);
  }
}
