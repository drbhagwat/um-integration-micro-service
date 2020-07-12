package api.external.item.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidSerialNumberUsed extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidSerialNumberUsed(String exception) {
		super(exception);
	}
}