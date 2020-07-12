package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelIsMandatoryAndCannotBeBlank extends Exception {
	public ChannelIsMandatoryAndCannotBeBlank(String exception) {
		super(exception);
	}
}