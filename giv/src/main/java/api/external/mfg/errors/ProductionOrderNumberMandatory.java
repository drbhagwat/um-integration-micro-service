package api.external.mfg.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductionOrderNumberMandatory extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ProductionOrderNumberMandatory(String exception) {
		super(exception);
	}
}