package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelNameMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelNameMaxLengthExceeded(String exception) {
    super(exception);
  }
}