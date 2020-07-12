package api.external.campaign.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents CampaignSearchCriteria fields.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignSearchCriteria {
	// campaign Code 
	private String campaignCode;
		
	//campaign Start Date
	private String startDate;
	
	//campaign status - active or not
	@JsonProperty("campaignStatus")
	private String isActive;
	
	// sortBy parameter - default is last_updated_date_time
	private String sortBy;
}