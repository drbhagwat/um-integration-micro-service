package api.external.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableQtyIsZero extends Exception {
	private static final long serialVersionUID = 1L;
	public AvailableQtyIsZero(String exception) {
    super(exception);
  }
}