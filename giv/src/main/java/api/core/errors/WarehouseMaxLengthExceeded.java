package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseMaxLengthExceeded(String exception) {
    super(exception);
  }
}