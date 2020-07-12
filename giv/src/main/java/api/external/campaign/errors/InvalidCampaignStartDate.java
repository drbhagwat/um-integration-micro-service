package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCampaignStartDate extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCampaignStartDate(String exception) {
    super(exception);
  }

}

