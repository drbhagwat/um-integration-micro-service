package api.external.campaign.errors;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActionFieldIsRequired extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ActionFieldIsRequired(String exception) {
		super(exception);
	}
}

