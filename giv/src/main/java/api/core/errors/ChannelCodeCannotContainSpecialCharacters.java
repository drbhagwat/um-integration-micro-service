package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelCodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public ChannelCodeCannotContainSpecialCharacters(String exception) {
    super(exception);
  }
}
