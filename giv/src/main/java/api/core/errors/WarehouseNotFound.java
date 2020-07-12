package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseNotFound extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseNotFound(String exception) {
    super(exception);
  }
}
