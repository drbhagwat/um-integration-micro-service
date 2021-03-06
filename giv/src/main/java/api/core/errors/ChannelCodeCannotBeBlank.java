package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelCodeCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelCodeCannotBeBlank(String exception) {
    super(exception);
  }
}
