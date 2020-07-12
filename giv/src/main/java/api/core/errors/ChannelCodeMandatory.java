package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelCodeMandatory extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelCodeMandatory(String exception) {
    super(exception);
  }
}
