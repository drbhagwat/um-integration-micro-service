package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCampaignActive extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCampaignActive(String exception) {
    super(exception);
  }

}

