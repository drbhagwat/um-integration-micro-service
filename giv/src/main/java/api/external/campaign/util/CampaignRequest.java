package api.external.campaign.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * provides validations(campaignActive, autoReplenish and action field)for campaign API
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Data
@NoArgsConstructor
@Component
public class CampaignRequest {
	@Value("${CAMPAIGN_ACTIVE}")
	private String[] campaignActive;

	@Value("${AUTO_REPLENISH}")
	private String[] autoReplenish;

	@Value("${CAMPAIGN_ACTION}")
	private String[] campaignAction;

	@Value("${CAMPAIGN_ACTIVE}")
	public void setCampaignActive(String[] campActv) {
		campaignActive = campActv;
	}

	@Value("${AUTO_REPLENISH}")
	public void setAUTOREPLENISH(String[] autReplnsh) {
		autoReplenish = autReplnsh;
	}

	@Value("${CAMPAIGN_ACTION}")
	public void setCampaignAction(String[] action) {
		campaignAction = action;
	}

	public boolean validateCampaignActive(String campActv) {
		String uppercaseCampaignActive = campActv.toUpperCase();
		return (uppercaseCampaignActive.equals(campaignActive[0]) || uppercaseCampaignActive.equals(campaignActive[1]))
				? true
				: false;
	}

	public boolean validateAutoReplenish(String autReplnsh) {
		String uppercaseAutoReplenish = autReplnsh.toUpperCase();
		return (uppercaseAutoReplenish.equals(autoReplenish[0]) || uppercaseAutoReplenish.equals(autoReplenish[1]))
				? true
				: false;
	}

	public boolean validateAction(String action) {
		String uppercaseCampaignAction = action.toUpperCase();
		return (uppercaseCampaignAction.equals(campaignAction[0]) || uppercaseCampaignAction.equals(campaignAction[1])) ? true
				: false;
	}
}