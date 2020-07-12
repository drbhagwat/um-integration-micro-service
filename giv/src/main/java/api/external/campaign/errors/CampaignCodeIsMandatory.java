package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignCodeIsMandatory extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CampaignCodeIsMandatory(String exception) {
    super(exception);
  }

}

