package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignCodeMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public CampaignCodeMaxLengthExceeded(String exception) {
		super(exception);
	}
}