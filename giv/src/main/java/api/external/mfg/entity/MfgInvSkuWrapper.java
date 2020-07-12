package api.external.mfg.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the wrapper class of the MFGInvSku
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since		2019-06-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfgInvSkuWrapper {
	private List<MfgInvSku> skuWrapper;
}
