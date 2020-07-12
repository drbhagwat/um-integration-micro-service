package api.external.item.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public ItemAlreadyExists(String exception) {
		super(exception);
	}
}