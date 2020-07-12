package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableQtyLessThanProtectQty extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AvailableQtyLessThanProtectQty(String exception) {
    super(exception);
  }
}