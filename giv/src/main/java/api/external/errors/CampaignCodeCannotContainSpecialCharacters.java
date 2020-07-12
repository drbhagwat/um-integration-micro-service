package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampaignCodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public CampaignCodeCannotContainSpecialCharacters(String exception) {
		super(exception);
	}
}
