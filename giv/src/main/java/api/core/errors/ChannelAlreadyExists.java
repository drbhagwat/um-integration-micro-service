package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelAlreadyExists extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelAlreadyExists(String exception) {
    super(exception);
  }
}
