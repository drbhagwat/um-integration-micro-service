package api.external.campaign.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignDetailNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CampaignDetailNotFound(String exception) {
		super(exception);
	}
}
