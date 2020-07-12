package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseCodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public WarehouseCodeCannotContainSpecialCharacters(String exception) {
    super(exception);
  }
}
