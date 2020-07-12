package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelCodeMaxLengthExceeded extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelCodeMaxLengthExceeded(String exception) {
    super(exception);
  }
}