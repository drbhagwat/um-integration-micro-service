package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignHeaderAlreadyExists extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CampaignHeaderAlreadyExists(String exception) {
    super(exception);
  }

}

