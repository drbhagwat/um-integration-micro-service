package api.external.campaign.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;
import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.entity.CampaignHeaderKey;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.entity.CampaignResponseHeader;
import api.external.campaign.search.CampaignSearchCriteria;
import api.external.campaign.search.CampaignSearchService;
import api.external.campaign.service.CampaignGetService;
import api.external.inventory.entity.SKUInventoryKey;

/**
 * Manages (Create,Read,Update and Search) operations for campaign_header_db and
 * campaign_detail_db Entity.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@RestController
public class CampaignController {
	@Autowired
	private CampaignGetService campaignGetService;

	@Autowired
	private CampaignSearchService campaignSearchService;
	 
	/**
	 * Retrieves all campaignheaders in the form of Pages.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignHeaderDb> - on success, returns a page of campaignheaders (could be empty). Otherwise, a global rest
	 * exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	
	@GetMapping("/campaignheaders")
	public Page<CampaignHeaderDb> getAllCampaignHeaders(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return campaignGetService.getAllCampaignHeaderDbs(pageNo, pageSize, sortBy, orderBy);
	}

	/**
	 * Retrieves all campaigndetails in the form of Pages.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignDetailDb> - on success, returns a page of campaigndetails (could be empty). Otherwise, a global rest
	 * exception handler is automatically called and a context-sensitive error message is displayed.
	 */	

	@GetMapping("/campaigndetails")
	public Page<CampaignDetailDb> getAllCampaignDetails(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
			@RequestParam(defaultValue = "D") String orderBy) {
		return campaignGetService.getAllCampaignDetailDbs(pageNo, pageSize, sortBy, orderBy);
	}

	/**
	 * This retrieves a single CampaignHeaderDb based on a composite primary key metioned below.
	 *
	 * @param channel    	- id of the channel
	 * @param campaignCode  - id of the campaign
	 * @return campaignHeaderDb - This returns an campaignheader based on criteria.
	 * @throws Exception - if the campaignheader does not exists
	 */
	
	@GetMapping("/campaignheaders/{channel},{campaignCode}")
	public CampaignHeaderDb getCampaignHeaderDb(@PathVariable String channel, @PathVariable String campaignCode)
			throws Exception {
		return campaignGetService.getCampaignHeaderDb(new CampaignHeaderKey(channel, campaignCode));
	}

	/**
     * This API is used to retrieve a single CampaignDetailDb based on a composite primary key mentioned below.
     *
     * @param company		  - id of the company
     * @param division		  - id of the division
     * @param warehouse		  - id of the warehouse
     * @param skuBarcode	  - represents the skuBarcode (usually a number)
     * @param season          - represents the season (winter or summer)
     * @param seasonYear      - represents the seasonYear (2019)
     * @param style           - represents the style of the item
     * @param styleSfx        - represents the style suffix of the item (if any)
     * @param color           - represents the color of the item
     * @param colorSfx        - represents the color suffix of the item (if any)
     * @param secDimension    - represents if the item is a second sale item
     * @param quality         - represents the quality of the item
     * @param sizeRngeCode    - represents the size range code
     * @param sizeRelPosnIn   - represents the size relative position in table
     * @param inventoryType   - represents the inventory type code
     * @param lotNumber       - represents the lot number in table
     * @param countryOfOrigin - represents the country of origin of the item
     * @param productStatus   - represents the product status of the item (if any)
     * @param skuAttribute1   - represents the skuAttribute1 of the item (if any)
     * @param skuAttribute2   - represents the skuAttribute2 of the item (if any)
     * @param skuAttribute3   - represents the skuAttribute3 of the item (if any)
     * @param skuAttribute4   - represents the skuAttribute4 of the item (if any)
     * @param skuAttribute5   - represents the skuAttribute5 of the item (if any)
     * @param channel    	  - id of the channel
	 * @param campaignCode    - id of the campaign
     * @return SkuInventory   - This returns an campaigndetail based on criteria.
     * @throws Exception - if the campaigndetail does not exists
     */
	
	@GetMapping("/campaigndetails/{company},{division},{warehouse},{skuBarcode},{season},{seasonYear},{style},{styleSfx},{color},{colorSfx},{secDimension},{quality},{sizeRngeCode},{sizeRelPosnIn},{inventoryType},{lotNumber},{countryOfOrigin},{productStatus},{skuAttribute1},{skuAttribute2},{skuAttribute3},{skuAttribute4},{skuAttribute5},{channel},{campaignCode}")
	public CampaignDetailDb getCampaignDetailDb(@PathVariable String company, @PathVariable String division,
			@PathVariable String warehouse, @PathVariable String skuBarcode, @PathVariable String season,
			@PathVariable String seasonYear, @PathVariable String style, @PathVariable String styleSfx,
			@PathVariable String color, @PathVariable String colorSfx, @PathVariable String secDimension,
			@PathVariable String quality, @PathVariable String sizeRngeCode, @PathVariable String sizeRelPosnIn,
			@PathVariable String inventoryType, @PathVariable String lotNumber, @PathVariable String countryOfOrigin,
			@PathVariable String productStatus, @PathVariable String skuAttribute1, @PathVariable String skuAttribute2,
			@PathVariable String skuAttribute3, @PathVariable String skuAttribute4, @PathVariable String skuAttribute5,
			@PathVariable String channel, @PathVariable String campaignCode) throws Exception {
		return campaignGetService.getCampaignDetailDb(new CampaignDetailKey(new SKUInventoryKey(company, division,
				warehouse, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality,
				sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1,
				skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5), channel, campaignCode));
	}
	
	/**
	 * This retrieves a single campaign based on a composite primary key metioned below.
	 *
	 * @param channel    	- id of the channel
	 * @param campaignCode  - id of the campaign
	 * @return CampaignRequestHeader - This returns the particular campaign(campaignheader associated with campaigndetail)found based on criteria.
	 * @throws Exception - if the campaign does not exists
	 */

	@GetMapping("/campaigns/{channel},{campaignCode}")
	public CampaignRequestHeader getCampaigns(@PathVariable String channel, @PathVariable String campaignCode)
			throws Exception {
		return campaignGetService.getCampaignDetailDb(new CampaignHeaderKey(channel, campaignCode));
	}

	 /**
	  * This API is used to create/update to/from campaign_header_db and campaign_detail_db.
	  *
	  * @param request - This is the json payload containing campaignRequest.
	  * @return CampaignResponseHeader - On success it returns HTTP status code 200.
	  * @throws Exception On failure, an exception is thrown and a meaningful error message is displayed with HTTP
	  *                   status code 400 (bad request).
	  */
	
	@PostMapping("/campaigns")
	public ResponseEntity<CampaignResponseHeader> campaign(@RequestBody @Valid CampaignRequestHeader request)
			throws Exception {
		return ResponseEntity.ok(campaignGetService.createOrUpdate(request));
	}

	/**
	 * @param campaignSearchCriteria - which you want to find the search fields in
	 *                               the campaign_header_db.
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, it returns to search the existing particular campaign
	 *         record found.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */
	@PostMapping("/campaignsearch")
	public Page<CampaignHeaderDb> searchCampaign(@RequestBody CampaignSearchCriteria campaignSearchCriteria,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "D") String orderBy) throws Exception {
		return campaignSearchService.searchCampaign(campaignSearchCriteria, pageNo, pageSize, orderBy);
	}
}