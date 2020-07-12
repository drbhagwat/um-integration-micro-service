package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SerialNumberMandatory extends Exception {
	public SerialNumberMandatory(String exception) {
		super(exception);
	}
}