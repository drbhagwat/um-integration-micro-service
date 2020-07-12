package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCampaignEndDate extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCampaignEndDate(String exception) {
    super(exception);
  }

}

