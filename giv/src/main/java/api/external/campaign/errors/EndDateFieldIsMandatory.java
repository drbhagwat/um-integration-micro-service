package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndDateFieldIsMandatory extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndDateFieldIsMandatory(String exception) {
    super(exception);
  }
}