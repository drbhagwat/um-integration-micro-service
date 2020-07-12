package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyCodeCannotContainSpecialCharacters extends Exception {
	private static final long serialVersionUID = 1L;
	public CompanyCodeCannotContainSpecialCharacters(String exception) {
    super(exception);
  }
}
