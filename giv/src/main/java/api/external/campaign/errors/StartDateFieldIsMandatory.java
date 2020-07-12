package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartDateFieldIsMandatory extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StartDateFieldIsMandatory(String exception) {
    super(exception);
  }
}