package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseCodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseCodeMandatory(String exception) {
    super(exception);
  }
}
