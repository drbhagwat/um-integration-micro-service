package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelNameCannotBeBlank extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelNameCannotBeBlank(String exception) {
    super(exception);
  }
}
