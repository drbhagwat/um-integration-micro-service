package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignIsNotAvailable extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CampaignIsNotAvailable(String exception) {
		super(exception);
	}
}
