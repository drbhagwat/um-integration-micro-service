package api.external.wms.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LockedQtyIsZero extends Exception {
	public LockedQtyIsZero(String exception) {
		super(exception);
	}
}