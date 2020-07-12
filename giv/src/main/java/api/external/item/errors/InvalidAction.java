package api.external.item.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidAction extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidAction(String exception) {
		super(exception);
	}
}