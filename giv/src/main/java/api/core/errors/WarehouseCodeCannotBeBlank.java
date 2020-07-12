package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseCodeCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseCodeCannotBeBlank(String exception) {
    super(exception);
  }
}
