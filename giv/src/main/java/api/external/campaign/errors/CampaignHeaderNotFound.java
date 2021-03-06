package api.external.campaign.errors;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignHeaderNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CampaignHeaderNotFound(String exception) {
		super(exception);
	}
}

