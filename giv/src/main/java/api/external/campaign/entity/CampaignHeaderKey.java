package api.external.campaign.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents CampaignHeaderDb's primary Key.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignHeaderKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	@NotNull
	private String channel;

	@NotBlank
	@NotNull
	private String campaignCode;
}