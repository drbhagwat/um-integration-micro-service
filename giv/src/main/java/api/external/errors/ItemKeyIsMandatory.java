package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemKeyIsMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ItemKeyIsMandatory(String exception) {
		super(exception);
	}
}