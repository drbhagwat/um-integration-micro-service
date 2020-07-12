package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NameCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public NameCannotContainSpecialCharacters(String exception) {
    super(exception);
  }
}
