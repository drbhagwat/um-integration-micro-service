package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignCodeCannotBeBlank extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CampaignCodeCannotBeBlank(String exception) {
    super(exception);
  }
}