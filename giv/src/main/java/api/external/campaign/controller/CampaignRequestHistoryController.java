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
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.service.CampaignRequestHistoryService;

/**
 * Manages (Read and Search) operations of campaign_request_history entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@RestController
public class CampaignRequestHistoryController {

	@Autowired
	private CampaignRequestHistoryService campaignRequestHistoryService;

	/**
	 * Retrieves all campaignrequesthistory in the form of Pages.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignRequestDetail> - on success, returns a page of campaignrequesthistory (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	
	@GetMapping("/campaignrequesthistory")
	public Page<CampaignRequestDetail> getAllCampaignRequestHistory(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (campaignRequestHistoryService.getAllCampaignRequestHistory(pageNo, pageSize, sortBy, orderBy));
	}

	/**
	 * This retrieves a single campaignrequesthistory based on a transaction number metioned below.
	 *
	 * @param transactionNumber    	- transactionNumber by which you want to find the campaign_request_history in the db
	 * @return campaignrequesthistory - This returns an campaignrequesthistory based on criteria.
	 * @throws Exception - if the campaignrequesthistory does not exists
	 */
	
	@GetMapping("/campaignrequesthistory/{transactionNumber}")
	public Page<CampaignRequestDetail> getCampaignRequestHistory(@PathVariable String transactionNumber,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return (campaignRequestHistoryService.getCampaignRequestHistory(transactionNumber, pageNo, pageSize, sortBy,
				orderBy));
	}

	/**
	 * @param searchCriteria - which you want to find the search fields in the
	 *                       campaign_request_history in the db.
	 * @param pageNo    - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, it returns to search the page of particular
	 *         campaign_request_history in the db.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */
	@PostMapping("/requestcampaignsearch")
	public Page<CampaignRequestDetail> searchCampaignRequestHistory(
			@RequestBody CampaignHistorySearchCriteria campaignHistorySearchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) {
		return campaignRequestHistoryService.searchCampaignRequestHistory(campaignHistorySearchCriteria, pageNo,
				pageSize, orderBy);
	}
}