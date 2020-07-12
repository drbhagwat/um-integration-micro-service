package api.external.wms.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmsInvSkuWrapper {
	private List<WmsSku> skuWrapper;
}
