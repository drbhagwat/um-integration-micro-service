package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignActiveFieldIsRequired extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CampaignActiveFieldIsRequired(String exception) {
    super(exception);
  }
}