package api.external.inventory.error;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllocatedQtyLessThanRequestedQty extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public AllocatedQtyLessThanRequestedQty(String exception) {
    super(exception);
  }
}