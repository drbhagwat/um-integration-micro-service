package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidStartDateInUpdate extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidStartDateInUpdate(String exception) {
    super(exception);
  }

}

