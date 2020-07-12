
package api.external.campaign.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import api.external.inventory.entity.SKUInventoryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents CampaignDetailDb's primary Key.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDetailKey implements Serializable  {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private SKUInventoryKey skuInventoryKey;
	
	@NotBlank
	@NotNull
	private String channel;

	@NotBlank
	@NotNull
	private String campaignCode;
}