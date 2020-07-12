package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseAlreadyExists(String exception) {
    super(exception);
  }
}
