package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidQueryTransactionsAction extends Exception {
	public InvalidQueryTransactionsAction(String exception) {
		super(exception);
	}
}