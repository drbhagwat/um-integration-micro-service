package api.external.campaign.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;
import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.entity.CampaignResponseHeader;
import api.external.campaign.entity.ResponseDetail;
import api.external.campaign.errors.AvailableQtyLessThanProtectQty;
import api.external.campaign.repo.CampaignDetailDbRepository;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.campaign.util.CampaignRequest;
import api.external.campaign.validation.CampaignValidationService;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.service.InvTransactionService;

/**
 * Provides update services for the Campaign Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Service
@Transactional
public class CampaignUpdateService {
	@Value("${INVALID_AUTO_REPLENISH}")
	private String invalidAutoReplenish;

	@Value("${CAMPAIGN_UPDATED}")
	private String campaignUpdated;

	@Value("${INVALID_MIN_QTY}")
	private String invalidMinQty;

	@Value("${INVALID_MAX_QTY}")
	private String invalidMaxQty;

	@Value("${INVALID_PROTECT_QTY}")
	private String invalidProtectQty;

	@Value("${SKU_ALREADY_EXISTS_IN_CAMPAIGN}")
	private String skuAlreadyExistsInCampaign;

	@Value("${MAX_QTY_LESS_THAN_MIN_QTY}")
	private String maxQtyLessThanMinQty;

	@Value("${AUTO_REPLENISH_FIELD_IS_REQUIRED}")
	private String autoReplenishFieldIsRequired;

	@Value("${AVAILABLE_QTY_IS_ZERO}")
	private String availableQtyIsZero;

	@Value("${CAMPAIGN_ACTIVE_YES}")
	private String campaignActiveYes;

	@Value("${PROTECT_QTY_EQUAL_TO_MAX_QTY}")
	private String protectQtyEqualToMaxQty;

	@Value("${INVALID_CAMPAIGN_HEADER}")
	private String invalidCampaignHeader;

	@Value("${AVAILABLE_QTY_LESS_THAN_REQUESTED}")
	private String availableQtyLessThanRequested;

	@Value("${CAMPAIGN_REQUEST_FULFILLED_WITH_HEADER}")
	private String campaignRequestFulfilledWithHeader;

	@Value("${FAILURE_RESPONSE}")
	private char f;

	@Value("${SUCCESS_RESPONSE}")
	private char s;

	@Value("${PARTIAL_RESPONSE}")
	private char p;

	@Value("${SUCCESS_SKUS}")
	private String successSkus;

	@Value("${FAILURE_SKUS}")
	private String failureSkus;

	@Value("${PARTIAL_SUCCESS_SKUS}")
	private String partialSuccessSkus;

	@Autowired
	private InvTransactionService invTransactionService;

	@Autowired
	private CampaignValidationService campaignValidationService;

	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	@Autowired
	private CampaignDetailDbRepository campaignDetailDbRepository;

	@Autowired
	private CampaignRequest campaignRequest;

	@Autowired
	private CampaignResponseHistoryService campaignResponseHistoryService;

	private CampaignDetailDb campaignDetailDb = null;
	private CampaignHeaderDb campaignHeaderDb = null;

	/**
	 * This method is used to process and validate entire campaign API request when
	 * the action is update
	 * 
	 * @param campaign - the campaign which you want to process.
	 * @return - on success, returns HTTP status code 200. On failure, returns HTTP
	 *         status code 400 (bad request).
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */
	public CampaignResponseHeader update(CampaignRequestHeader campaign) throws Exception {
		CampaignResponseHeader campaignResponseHeader = null;
		ResponseDetail responseDetail = null;
		SKUInventoryKey skuInventoryKey = null;

		// get the details
		List<CampaignRequestDetail> campaignDetails = campaign.getCampaignDetails();

		// calculate detail size
		int totalNumberOfDetails = campaignDetails.size();

		// init variables for various statistics
		int numberOfFailedDetails = 0;
		int numberOfPassedDetails = 0;
		int numberOfPartialPassedDetails = 0;
		char responseCode = 0;
		String responseId = null;

		// init headerSaved flag to false before you enter the loop
		boolean isHeaderInitialized = false;
		boolean validHeader = false;
		List<String> processedSkuList = new ArrayList<>();
		List<CampaignDetailDb> campignsList = new ArrayList<>();
		List<CampaignDetailDb> campaignDb = null;

		// get all campaignheader fields
		String action = campaign.getAction();
		String channel = campaign.getChannel();
		String campaignCode = campaign.getCampaignCode();
		String user = campaign.getUser();
		LocalDate ldtStartDate = campaign.getCampaignStartDate();
		LocalDate ldtEndDate = campaign.getCampaignEndDate();
		LocalDateTime dateTimeStamp = campaign.getDateTimeStamp();
		LocalDate currentDate = LocalDate.now();

		// init the detail fields
		String skuBarcode, autoReplenish;
		int minQty, maxQty, protectQty;

		// campaign header field alone could be updated without specifying any SKUs
		if (totalNumberOfDetails == 0) {
			try {
				// validate campaign header fields
				campaignHeaderDb = campaignValidationService.validateCampaignHeader(campaign);
				campaignDb = campaignHeaderDb.getCampaignDbDetails();
				// save header fields to campaignheaderdb
				campaignHeaderDbRepository.save(campaignHeaderDb);

				responseDetail = new ResponseDetail();
				responseId = campaignRequestFulfilledWithHeader;
				responseDetail.setResponseCode(s);
				responseDetail.setResponseId(responseId);

				campaignResponseHeader = new CampaignResponseHeader();
				campaignResponseHistoryService.initHeaderCampaignResponseHistory(campaignResponseHeader, campaign);
				campaignResponseHeader.setResponseDetail(responseDetail);

				if ((currentDate.compareTo(ldtStartDate) == 0)) {

					responseDetail = new ResponseDetail();
					responseDetail.setResponseCode(s);
					responseDetail.setResponseId(campaignUpdated);
					return campaignResponseHistoryService.saveDetailDb(campaignResponseHeader, campaignDb,
							responseDetail);
				} else {
					return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails, responseDetail);
				}
			} catch (Exception exception) {
				String message = exception.getMessage();
				campaignResponseHeader = new CampaignResponseHeader();
				campaignResponseHistoryService.initHeaderCampaignResponseHistory(campaignResponseHeader, campaign);
				responseDetail = new ResponseDetail();
				responseDetail.setResponseCode(f);
				responseDetail.setResponseId(message);
				campaignResponseHeader.setResponseDetail(responseDetail);

				if ((currentDate.compareTo(ldtStartDate) == 0)) {
					responseDetail = new ResponseDetail();
					responseDetail.setResponseCode(f);
					responseDetail.setResponseId(message);

					if (campaignDb != null) {
						return campaignResponseHistoryService.saveDetailDb(campaignResponseHeader, campaignDb,
								responseDetail);
					} else {
						return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails,
								responseDetail);
					}
				} else {
					return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails, responseDetail);
				}
			}
		} else { // campaign header field could be updated with SKUs
			for (CampaignRequestDetail campaignDetail : campaignDetails) {
				try {
//					campaignHeaderDb = campaignHeaderDbRepository.findByCampaignCode(campaign.getCampaignCode());
//					// validate campaignheader is exists or not in the update request
//					if (campaignHeaderDb == null) {
//						throw new CampaignHeaderNotFound(invalidCampaignHeader);
//					}
					// perform header validation campaignheader only once for efficiency reasons
					if (!isHeaderInitialized) {
						// validate campaignheader fileds
						campaignHeaderDb = campaignValidationService.validateCampaignHeader(campaign);

						if (campaignHeaderDb != null) {
							validHeader = true;
						}
						isHeaderInitialized = true;
					}

					if (currentDate.compareTo(ldtStartDate) == 0 && campaignHeaderDb.getActive().equalsIgnoreCase("N")
							&& totalNumberOfDetails > 0 && (ldtEndDate.compareTo(currentDate) == 0)) {
						campaignResponseHeader = new CampaignResponseHeader();
						campaignResponseHistoryService.initHeaderCampaignResponseHistory(campaignResponseHeader,
								campaign);

						responseDetail = new ResponseDetail();
						responseId = campaignRequestFulfilledWithHeader;
						responseDetail.setResponseCode(s);
						responseDetail.setResponseId(responseId);
						campaignResponseHeader.setResponseDetail(responseDetail);

						return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails,
								responseDetail);
					}

					// initialize the campaigndetail fields
					skuBarcode = campaignDetail.getSkuBarcode();
					minQty = campaignDetail.getMinQty();
					maxQty = campaignDetail.getMaxQty();
					protectQty = campaignDetail.getProtectQty();
					autoReplenish = campaignDetail.getAutoReplenish();

					// validate campaigndetailkey fields(23 key fields)
					campaignDetailDb = campaignValidationService.validateCampaignDetailKeys(campaign, campaignDetail);
					skuInventoryKey = campaignDetailDb.getId().getSkuInventoryKey();

					CampaignDetailKey tempCampaignDetailKey = new CampaignDetailKey(skuInventoryKey, channel,
							campaignCode);

					campaignDetailDb = campaignDetailDbRepository.findById(tempCampaignDetailKey);

					// validate skuBarcode is already exists or not for particular campaign
					if (processedSkuList.contains(skuBarcode)) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = skuAlreadyExistsInCampaign;
						continue;
					}
					processedSkuList.add(skuBarcode);

					// validate minQty should be positive
					if (minQty < 0) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = invalidMinQty;
						continue;
					}

					// validate maxQty should be positive
					if (maxQty < 0) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = invalidMaxQty;
						continue;
					}

					// validate protectQty should be positive
					if (protectQty < 0) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = invalidProtectQty;
						continue;
					}

					// validate protectQty should be equal to maxQty
					if (protectQty != maxQty) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = protectQtyEqualToMaxQty;
						continue;
					}

					// validate maxQty should be greater than minQty
					if (maxQty < minQty) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = maxQtyLessThanMinQty;
						continue;
					}

					// validate autoReplenish parameter should be mandatory
					if (autoReplenish == null) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = autoReplenishFieldIsRequired;
						continue;
					}

					// validate autoReplenish - should be Y or N
					if (!campaignRequest.validateAutoReplenish(autoReplenish)) {
						numberOfFailedDetails++;
						responseCode = f;
						responseId = invalidAutoReplenish;
						continue;
					}
					// check if the campaign is currently running
					if (campaignHeaderDb.getActive().equalsIgnoreCase(campaignActiveYes)) {

						if (campaignDetailDb == null) {
							campaignDetailDb = new CampaignDetailDb();
							campaignDetailDb.setId(tempCampaignDetailKey);

							// set the detail fields to CampaignDetailDb
							campaignDetailDb.setMaximumQuantity(maxQty);
							campaignDetailDb.setMinimumQuantity(minQty);
							campaignDetailDb.setOriginalProtectQuantity(protectQty);
							campaignDetailDb.setAutoReplenish(autoReplenish);
							campaignDetailDb.setCampaignHeaderDb(campaignHeaderDb);

							campaignValidationService.inventoryUpdation(campaign, protectQty, campaignDetailDb);

						} else {// existing campaign detail
							int originalProtectDb = campaignDetailDb.getOriginalProtectQuantity();
							int currentProtectDb = campaignDetailDb.getCurrentProtectQty();

							campaignDetailDb.setMaximumQuantity(maxQty);
							campaignDetailDb.setMinimumQuantity(minQty);
							campaignDetailDb.setOriginalProtectQuantity(protectQty);
							campaignDetailDb.setAutoReplenish(autoReplenish);
							campaignDetailDb.setCampaignHeaderDb(campaignHeaderDb);

							if (originalProtectDb != protectQty) {
								int replenishQty = currentProtectDb - protectQty;
								if (replenishQty > 0) {
									campaignValidationService.inventoryReplenishment(campaign,
											replenishQty, protectQty, campaignDetailDb);
								} else {
									if (replenishQty == 0) {
										invTransactionService.updateDuringReplenishment(skuInventoryKey, campaignCode,
												channel, user, dateTimeStamp, action, replenishQty);
									} else {
										int newReplenish = Math.abs(replenishQty);
										campaignValidationService.inventoryUpdation(campaign, newReplenish, campaignDetailDb);
									}
								}
							}
						}
					} else { // check if the campaign is currently not running
						if (campaignDetailDb == null) {
							// validate campaigndetail is exists or not in the update request, if it is not
							// exists add new campaigndetail for that campaignheader
							campaignDetailDb = new CampaignDetailDb();
							campaignDetailDb.setId(tempCampaignDetailKey);
						} // set the detail fields to CampaignDetailDb
						campaignDetailDb.setMaximumQuantity(maxQty);
						campaignDetailDb.setMinimumQuantity(minQty);
						campaignDetailDb.setOriginalProtectQuantity(protectQty);
						campaignDetailDb.setAutoReplenish(autoReplenish);
						campaignDetailDb.setCampaignHeaderDb(campaignHeaderDb);
					}
					numberOfPassedDetails++;
					responseCode = s;
					responseId = campaignUpdated;

					campignsList.add(campaignDetailDb);
				} catch (AvailableQtyLessThanProtectQty exception) {
					numberOfPartialPassedDetails++;
					responseCode = 'P';
					responseId = exception.getMessage();
				} catch (Exception exception) {
					numberOfFailedDetails++;
					responseCode = 'F';
					responseId = exception.getMessage();
				} finally {
					responseDetail = new ResponseDetail();
					responseDetail.setResponseCode(responseCode);
					responseDetail.setResponseId(responseId);
					campaignDetail.setResponseDetail(responseDetail);

					campaignResponseHeader = new CampaignResponseHeader();
					campaignResponseHistoryService.initHeaderCampaignResponseHistory(campaignResponseHeader, campaign);
				}
			} // end-for
		} // end-else
			// save the header and detail in db
		if (validHeader && !campignsList.isEmpty()) {
			campaignHeaderDb.setCampaignDbDetails(campignsList);
			campaignHeaderDbRepository.save(campaignHeaderDb);
		}
		// This is header level response object.
		responseDetail = new ResponseDetail();

		if (numberOfPartialPassedDetails != 0) {
			responseId = partialSuccessSkus;
			responseDetail.setResponseCode(p);
			responseDetail.setResponseId(responseId);
		} else {
			if (numberOfFailedDetails == 0) {
				responseId = successSkus;
				responseDetail.setResponseCode(s);
				responseDetail.setResponseId(responseId);
			} else {
				if (numberOfPassedDetails == 0) {
					responseId = failureSkus;
					responseDetail.setResponseCode(f);
					responseDetail.setResponseId(responseId);
				} else {
					responseId = partialSuccessSkus;
					responseDetail.setResponseCode(p);
					responseDetail.setResponseId(responseId);
				}
			}
		}
		campaignResponseHeader.setResponseDetail(responseDetail);
		return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails, responseDetail);
	}
}