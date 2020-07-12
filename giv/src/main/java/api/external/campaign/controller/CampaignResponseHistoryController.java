
package api.external.campaign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.campaign.entity.CampaignHistorySearchCriteria;
import api.external.campaign.entity.CampaignResponseDetail;
import api.external.campaign.service.CampaignResponseHistoryService;

/**
 * Manages (Create, Read and Search) operations of campaign_response_history entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@RestController
public class CampaignResponseHistoryController {

	@Autowired
	private CampaignResponseHistoryService campaignResponseHistoryService;
	/**
	 * Retrieves all campaignresponsehistory in the form of Pages.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignResponseDetail> - on success, returns a page of campaignresponsehistory (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */

	@GetMapping("/campaignresponsehistory")
	public Page<CampaignResponseDetail> getAllCampaignResponseHistory(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (campaignResponseHistoryService.getAllCampaignResponseHistory(pageNo, pageSize, sortBy, orderBy));
	}
	
	/**
	 * This retrieves a single campaignresponsehistory based on a transaction number metioned below.
	 *
	 * @param transactionNumber    	- transactionNumber by which you want to find the campaign_response_history in the db
	 * @return campaignresponsehistory - This returns an campaignresponsehistory based on criteria.
	 * @throws Exception - if the campaignresponsehistory does not exists
	 */
	
	@GetMapping("/campaignresponsehistory/{transactionNumber}")
	public Page<CampaignResponseDetail> getCampaignResponseHistory(@PathVariable String transactionNumber,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (campaignResponseHistoryService.getCampaignResponseHistory(transactionNumber, pageNo, pageSize,
				sortBy, orderBy));
	}
	/**
	 * @param searchCriteria - which you want to find the search fields in the
	 *                       campaign_response_history in the db.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, it returns to search the page of particular
	 *         campaign_response_history in the db.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */

	@PostMapping("/responsecampaignsearch")
	public Page<CampaignResponseDetail> searchCampaignResponseHistory(
			@RequestBody CampaignHistorySearchCriteria campaignHistorySearchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, 
			@RequestParam(defaultValue = "D") String orderBy) {
		return campaignResponseHistoryService.searchCampaignResponseHistory(campaignHistorySearchCriteria, pageNo,
				pageSize, orderBy);
	}
}