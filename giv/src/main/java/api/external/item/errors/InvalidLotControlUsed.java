package api.external.item.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidLotControlUsed extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidLotControlUsed(String exception) {
		super(exception);
	}
}