package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateTimeStampFieldIsRequired extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateTimeStampFieldIsRequired(String exception) {
    super(exception);
  }
}