package api.external.item.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemNotFound extends Exception {
	private static final long serialVersionUID = 1L;
	public ItemNotFound(String exception) {
		super(exception);
	}
}