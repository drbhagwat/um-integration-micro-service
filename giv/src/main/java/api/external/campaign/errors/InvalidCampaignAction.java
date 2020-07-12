package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCampaignAction extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCampaignAction(String exception) {
    super(exception);
  }

}

