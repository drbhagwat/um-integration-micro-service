package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuEmpty extends Exception {
	private static final long serialVersionUID = 1L;
	public SkuEmpty(String exception) {
		super(exception);
	}
}