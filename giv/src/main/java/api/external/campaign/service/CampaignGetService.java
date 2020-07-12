package api.external.campaign.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import api.core.errors.KeyFieldCannotBeBlank;
import api.core.errors.KeyFieldMandatory;
import api.core.errors.TransactionNumberCannotBeBlank;
import api.core.errors.TransactionNumberMandatory;
import api.core.validation.CodeValidationService;
import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignDetailKey;
import api.external.campaign.entity.CampaignHeaderDb;
import api.external.campaign.entity.CampaignHeaderKey;
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.entity.CampaignResponseHeader;
import api.external.campaign.entity.ResponseDetail;
import api.external.campaign.errors.ActionFieldIsRequired;
import api.external.campaign.errors.InvalidCampaignAction;
import api.external.campaign.repo.CampaignDetailDbRepository;
import api.external.campaign.repo.CampaignHeaderDbRepository;
import api.external.campaign.repo.CampaignResponseHeaderRepository;
import api.external.campaign.util.CampaignRequest;
import api.external.campaign.validation.CampaignValidationService;

/**
 * Provides create, read and update services for the Campaign Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Service
public class CampaignGetService {
	@Value("${CREATE_ACTION}")
	private String createAction;

	@Value("${UPDATE_ACTION}")
	private String updateAction;

	@Value("${TRANSACTION_NUMBER_MANDATORY}")
	private String transactionNumberMandatory;

	@Value("${TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
	private String transactionNumberCannotBeBlank;

	@Value("${INVALID_CAMPAIGN_ACTION}")
	private String invalidCampaignAction;

	@Value("${ACTION_FIELD_IS_REQUIRED}")
	private String actionFieldIsRequired;

	@Value("${FAILURE_RESPONSE}")
	private char f;

	@Autowired
	private CampaignResponseHistoryService campaignResponseHistoryService;

	@Autowired
	private CampaignRequest campaignRequest;

	@Autowired
	private CampaignValidationService campaignValidationService;

	@Autowired
	private CampaignHeaderDbRepository campaignHeaderDbRepository;

	@Autowired
	private CampaignDetailDbRepository campaignDetailDbRepository;

	@Autowired
	private CodeValidationService codeValidationService;

	@Autowired
	private CampaignResponseHeaderRepository campaignResponseHeaderRepository;

	@Autowired
	private CampaignRequestHistoryService campaignRequestHistoryService;

	@Autowired
	private CampaignCreateService campaignCreateService;

	@Autowired
	private CampaignUpdateService campaignUpdateService;

	// This method is to create/update in/from campaignheader and campaigndetail
	public CampaignResponseHeader createOrUpdate(CampaignRequestHeader campaign) throws Exception {
		CampaignResponseHeader campaignResponseHeader = null;
		ResponseDetail responseDetail = null;

		String transactionNumber = campaign.getTransactionNumber();
		String action = campaign.getAction();

		// get the details
		List<CampaignRequestDetail> campaignDetails = campaign.getCampaignDetails();

		try {
			campaignResponseHeader = campaignResponseHeaderRepository.findByTransactionNumber(transactionNumber);
	        // if transaction number exists, return the existing response
			if (campaignResponseHeader != null) {
				return campaignResponseHeader;
			}
	        // save everything to request history.
			campaignRequestHistoryService.save(campaign);

			if (action == null) {
				throw new ActionFieldIsRequired(actionFieldIsRequired);
			}
			// validate action - should be C or U
			if (!campaignRequest.validateAction(action)) {
				throw new InvalidCampaignAction(invalidCampaignAction);
			}// if the action is C, call create service and perform all operations
			if (action.toUpperCase().equals(createAction)) {
				campaignResponseHeader = campaignCreateService.create(campaign);
			} else { // if the action is U, call update service and perform all operations
				campaignResponseHeader = campaignUpdateService.update(campaign);
			}
			return campaignResponseHeader;
		} catch (Exception exception) {
			responseDetail = new ResponseDetail();
			responseDetail.setResponseCode(f);
			responseDetail.setResponseId(exception.getMessage());

			campaignResponseHeader = new CampaignResponseHeader();
			campaignResponseHistoryService.initHeaderCampaignResponseHistory(campaignResponseHeader, campaign);
			campaignResponseHeader.setResponseDetail(responseDetail);
			return campaignResponseHistoryService.save(campaignResponseHeader, campaignDetails, responseDetail);
		}
	}

	/**
	 * This method is used to get all campaignheaders.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignHeaderDb> - on success, returns a page of campaignheaders (could be empty). Otherwise, a global rest
	 * exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	public Page<CampaignHeaderDb> getAllCampaignHeaderDbs(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignHeaderDbRepository.findAll(paging);
	}

	/**
	 * This method is used to get all campaigndetails.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignDetailDb> - on success, returns a page of campaigndetails (could be empty). Otherwise, a global rest
	 * exception handler is automatically called and a context-sensitive error message is displayed.
	 */	

	public Page<CampaignDetailDb> getAllCampaignDetailDbs(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignDetailDbRepository.findAll(paging);
	}
	
	/**
	 *  This method is used to get specific campaignheader with campaignheaderkeys(channel and campaignCode)
	 *  
	 * @param campaignHeaderKey - campaignHeaderKey based on which you want to retrieve the campaignheader from the db.
	 * @return - on success, it returns the particular campaignheader existing in db.	
	 *  @throws Exception - suitable exception if the campaignheader is not found.
	 */
	
	public CampaignHeaderDb getCampaignHeaderDb(CampaignHeaderKey campaignHeaderKey) throws Exception {
		return campaignValidationService.validateHeaderId(campaignHeaderKey);
	}

	/**
	 *  This method is used to get specific campaigndetail with campaigndetailkeys(company, division, warehouse, skuBarcode, season, seasonYear, style, styleSfx, color, colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn, inventoryType, lotNumber, countryOfOrigin, productStatus, skuAttribute1, skuAttribute2, skuAttribute3, skuAttribute4, skuAttribute5, channel, campaignCode)
	 *  
	 * @param campaignDetailKey - campaigndetailKey based on which you want to retrieve the campaigndetail from the db.
	 * @return - on success, it returns the particular campaigndetail existing in db.	
	 *  @throws Exception - suitable exception if the campaigndetail is not found.
	 */

	public CampaignDetailDb getCampaignDetailDb(CampaignDetailKey campaignDetailKey) throws Exception {
		return campaignValidationService.validateDetailId(campaignDetailKey);
	}

	/**
	 * This method is used to get particular campaign with campaignheaderkeys(channel and campaignCode)	 
	 *   
	 * @param campaignHeaderKey - campaignHeaderKey based on which you want to retrieve the campaignheader from the db.
	 * @return - on success, it returns the particular campaign(campaignheader associated with campaigndetail) found.	 
	 * @throws Exception - suitable exception if the campaign is not found.
	 */
	
	public CampaignRequestHeader getCampaignDetailDb(CampaignHeaderKey campaignHeaderKey) throws Exception {
		// validate campaignheader with key fields(channel and campaignCode)
		campaignValidationService.validateHeaderId(campaignHeaderKey);
		CampaignHeaderDb campaignHeaderDb = campaignHeaderDbRepository
				.findById(new CampaignHeaderKey(campaignHeaderKey.getChannel(), campaignHeaderKey.getCampaignCode()));

		List<CampaignDetailDb> campaignDetailDbs = campaignHeaderDb.getCampaignDbDetails();

		CampaignRequestHeader campaignRequestHeader = new CampaignRequestHeader();

		String channel = campaignHeaderDb.getId().getChannel();
		campaignRequestHeader.setChannel(channel);

		String campaignCode = campaignHeaderDb.getId().getCampaignCode();
		campaignRequestHeader.setCampaignCode(campaignCode);

		LocalDate campaignStartDate = campaignHeaderDb.getStartDate();
		campaignRequestHeader.setCampaignStartDate(campaignStartDate);

		LocalDate campaignEndDate = campaignHeaderDb.getEndDate();
		campaignRequestHeader.setCampaignEndDate(campaignEndDate);

		String campaignActive = campaignHeaderDb.getActive();
		campaignRequestHeader.setCampaignActive(campaignActive);

		String action = campaignHeaderDb.getAction();
		campaignRequestHeader.setAction(action);

		String tempUser = campaignHeaderDb.getUser();
		campaignRequestHeader.setUser(tempUser);

		LocalDateTime tempDateTimeStamp = campaignHeaderDb.getDateTimeStamp();
		campaignRequestHeader.setDateTimeStamp(tempDateTimeStamp);

		List<CampaignRequestDetail> details = new ArrayList<>();

		for (CampaignDetailDb campaignDetailDb : campaignDetailDbs) {

			CampaignRequestDetail campaignRequestDetail = new CampaignRequestDetail();

			String company = campaignDetailDb.getId().getSkuInventoryKey().getCompany();
			campaignRequestDetail.setCompany(company);

			String division = campaignDetailDb.getId().getSkuInventoryKey().getDivision();
			campaignRequestDetail.setDivision(division);

			String warehouse = campaignDetailDb.getId().getSkuInventoryKey().getWarehouse();
			campaignRequestDetail.setWarehouse(warehouse);

			String tempSkuBarcode = campaignDetailDb.getId().getSkuInventoryKey().getSkuBarcode();
			campaignRequestDetail.setSkuBarcode(tempSkuBarcode);

			String season = campaignDetailDb.getId().getSkuInventoryKey().getSeason();
			campaignRequestDetail.setSeason(season);

			String seasonYear = campaignDetailDb.getId().getSkuInventoryKey().getSeasonYear();
			campaignRequestDetail.setSeasonYear(seasonYear);

			String style = campaignDetailDb.getId().getSkuInventoryKey().getStyle();
			campaignRequestDetail.setStyle(style);

			String styleSfx = campaignDetailDb.getId().getSkuInventoryKey().getStyleSfx();
			campaignRequestDetail.setStyleSfx(styleSfx);

			String color = campaignDetailDb.getId().getSkuInventoryKey().getColor();
			campaignRequestDetail.setColor(color);

			String colorSfx = campaignDetailDb.getId().getSkuInventoryKey().getColorSfx();
			campaignRequestDetail.setColorSfx(colorSfx);

			String secDimension = campaignDetailDb.getId().getSkuInventoryKey().getSecDimension();
			campaignRequestDetail.setSecDimension(secDimension);

			String quality = campaignDetailDb.getId().getSkuInventoryKey().getQuality();
			campaignRequestDetail.setQuality(quality);

			String sizeRngeCode = campaignDetailDb.getId().getSkuInventoryKey().getSizeRngeCode();
			campaignRequestDetail.setSizeRngeCode(sizeRngeCode);

			String sizeRelPosnIn = campaignDetailDb.getId().getSkuInventoryKey().getSizeRelPosnIn();
			campaignRequestDetail.setSizeRelPosnIn(sizeRelPosnIn);

			String inventoryType = campaignDetailDb.getId().getSkuInventoryKey().getInventoryType();
			campaignRequestDetail.setInventoryType(inventoryType);

			String lotNumber = campaignDetailDb.getId().getSkuInventoryKey().getLotNumber();
			campaignRequestDetail.setLotNumber(lotNumber);

			String countryOfOrigin = campaignDetailDb.getId().getSkuInventoryKey().getCountryOfOrigin();
			campaignRequestDetail.setCountryOfOrigin(countryOfOrigin);

			String productStatus = campaignDetailDb.getId().getSkuInventoryKey().getProductStatus();
			campaignRequestDetail.setProductStatus(productStatus);

			String skuAttribute1 = campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute1();
			campaignRequestDetail.setSkuAttribute1(skuAttribute1);

			String skuAttribute2 = campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute2();
			campaignRequestDetail.setSkuAttribute2(skuAttribute2);

			String skuAttribute3 = campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute3();
			campaignRequestDetail.setSkuAttribute3(skuAttribute3);

			String skuAttribute4 = campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute4();
			campaignRequestDetail.setSkuAttribute4(skuAttribute4);

			String skuAttribute5 = campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute5();
			campaignRequestDetail.setSkuAttribute5(skuAttribute5);

			int minQty = campaignDetailDb.getMinimumQuantity();
			campaignRequestDetail.setMinQty(minQty);

			int maxQty = campaignDetailDb.getMaximumQuantity();
			campaignRequestDetail.setMaxQty(maxQty);

			int tempProtectQty = campaignDetailDb.getOriginalProtectQuantity();
			campaignRequestDetail.setProtectQty(tempProtectQty);

			String tempAutoReplenish = campaignDetailDb.getAutoReplenish();
			campaignRequestDetail.setAutoReplenish(tempAutoReplenish);

			details.add(campaignRequestDetail);
		}
		campaignRequestHeader.getCampaignDetails().addAll(details);
		return campaignRequestHeader;
	}
}