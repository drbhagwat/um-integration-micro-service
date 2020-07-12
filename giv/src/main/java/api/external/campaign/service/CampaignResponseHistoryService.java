package api.external.campaign.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import api.external.campaign.entity.CampaignDetailDb;
import api.external.campaign.entity.CampaignHistorySearchCriteria;
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.entity.CampaignResponseDetail;
import api.external.campaign.entity.CampaignResponseHeader;
import api.external.campaign.entity.ResponseDetail;
import api.external.campaign.repo.CampaignResponseDetailRepository;
import api.external.campaign.repo.CampaignResponseHeaderRepository;
import api.external.util.Converter;

/**
 * provides create, read and search services for the campaignResponseHistory Controller.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Service
@Transactional
public class CampaignResponseHistoryService {
	
	@Value("${CREATE_ACTION}")
	private String createAction;

	@Value("${UPDATE_ACTION}")
	private String updateAction;
	
	@Autowired
	private CampaignResponseHeaderRepository campaignResponseHeaderRepository;

	@Autowired
	private CampaignResponseDetailRepository campaignResponseDetailRepository;

	/**
	 * This method is used to get all campaignresponsehistory.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignResponseDetail> - on success, returns a page of campaignresponsehistory (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */
	public Page<CampaignResponseDetail> getAllCampaignResponseHistory(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignResponseDetailRepository.findAll(paging);
	}

	/**
	 *  This method is used to get specific campaignresponsehistory based on transactionNumber
	 *
	 * @param transactionNumber    	- transactionNumber by which you want to find the campaign_response_history in the db
	 * @return campaignresponsehistory - This returns an campaignresponsehistory based on criteria.
	 * @throws Exception - if the campaignresponsehistory does not exists
	 */

	public Page<CampaignResponseDetail> getCampaignResponseHistory(String transactionNumber, Integer pageNo,
			Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignResponseDetailRepository.findByTransactionNumber(transactionNumber, paging);
	}

	/**
	 * @param searchCriteria - which you want to find the search fields in the
	 *                       campaign_response_history db.
	 * @param pageNo    - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, it returns to search the page of particular
	 *         campaign_response_history record found
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */

	public Page<CampaignResponseDetail> searchCampaignResponseHistory(
			CampaignHistorySearchCriteria campaignHistorySearchCriteria, Integer pageNo, Integer pageSize,
			String orderBy) {

		// initialize the search criteria fields
		String transactionNumber = campaignHistorySearchCriteria.getTransactionNumber();
		String warehouse = campaignHistorySearchCriteria.getWarehouse();
		String channel = campaignHistorySearchCriteria.getChannel();
		String campaignCode = campaignHistorySearchCriteria.getCampaignCode();
		String skuBarcode = campaignHistorySearchCriteria.getSkuBarcode();
		String sortBy = campaignHistorySearchCriteria.getSortBy();

		// handle both wild card and when the search parameter is not present in
		// campaign search criteria
		if ((transactionNumber == null) || transactionNumber.equals("*") || transactionNumber.equals("")) {
			transactionNumber = "";
		} else {
			transactionNumber = transactionNumber.trim();
		}

		if ((sortBy == null) || (sortBy.equals("*")) || (sortBy.trim().equals(""))) {
			sortBy = "lastUpdatedDateTime";
		} else { // convert the user supplied JSON parameter to equivalent db parameter
			sortBy = Converter.toDatabaseColumnName(sortBy);
		}

		if ((channel == null) || channel.equals("*") || channel.equals("")) {
			channel = "";
		} else {
			channel = channel.trim();
		}
		if ((campaignCode == null) || campaignCode.equals("*") || campaignCode.equals("")) {
			campaignCode = "";
		} else {
			campaignCode = campaignCode.trim();
		}

		if ((warehouse == null) || warehouse.equals("*") || warehouse.equals("")) {
			warehouse = "";
		} else {
			warehouse = warehouse.trim();
		}

		if ((skuBarcode == null) || (skuBarcode.equals("*") || skuBarcode.equals(""))) {
			skuBarcode = "";
		} else {
			skuBarcode = skuBarcode.trim();
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String userSuppliedStartDate = campaignHistorySearchCriteria.getUserSuppliedStartDate();
		LocalDateTime ldtUserSuppliedStartDate = null;
		LocalDate ldateUserSuppledStartDate = null;

		if ((userSuppliedStartDate == null) || userSuppliedStartDate.equals("*") || userSuppliedStartDate.equals("")) {
			// if the userSuppliedStartDate is not given, take the userSuppliedStartDate as 31stDecember2010 as default with time (00:00)
			ldtUserSuppliedStartDate = LocalDate.of(2010, 12, 31).atStartOfDay();
		} else {
			ldateUserSuppledStartDate = LocalDate.parse(userSuppliedStartDate, dateTimeFormatter);
			// if the userSuppliedStartDate is given, take the userSuppliedStartDate from
			// the start of the day with time (00:00)
			ldtUserSuppliedStartDate = ldateUserSuppledStartDate.atStartOfDay();
		}

		String userSuppliedEndDate = campaignHistorySearchCriteria.getUserSuppliedEndDate();
		LocalDateTime ldtUserSuppliedEndDate = null;
		LocalDate ldateUserSuppledEndDate = null;

		if ((userSuppliedEndDate == null) || userSuppliedEndDate.equals("*") || userSuppliedEndDate.equals("")) {
			// if the userSuppliedEndDate is not given, take the userSuppliedEndDate as todays date with time (23:59:59.9999999)
			ldtUserSuppliedEndDate = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
		} else {
			ldateUserSuppledEndDate = LocalDate.parse(userSuppliedEndDate, dateTimeFormatter);
			// if the userSuppliedEndDate is given, take the userSuppliedEndDate upto end of
			// the day with time (23:59:59.9999999)
			ldtUserSuppliedEndDate = ldateUserSuppledEndDate.atTime(LocalTime.MAX);
		}
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

		Page<CampaignResponseDetail> details = campaignResponseDetailRepository
				.findByTransactionNumberIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndCampaignCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, warehouse, channel, campaignCode, skuBarcode,
						ldtUserSuppliedStartDate, ldtUserSuppliedEndDate);
		return details;
	}

	/*
	 * This method is to set the header fields in the campaign response history
	 */
	public void initHeaderCampaignResponseHistory(CampaignResponseHeader campaignResponseHeader,
			CampaignRequestHeader campaignHeader) {

		campaignResponseHeader.setTransactionNumber(campaignHeader.getTransactionNumber());
		campaignResponseHeader.setChannel(campaignHeader.getChannel());
		
		if(campaignHeader.getCampaignCode() != null) {
		campaignResponseHeader.setCampaignCode(campaignHeader.getCampaignCode().stripTrailing());
		}else {
			campaignResponseHeader.setCampaignCode(campaignHeader.getCampaignCode());
		}
		campaignResponseHeader.setCampaignStartDate(campaignHeader.getCampaignStartDate());
		campaignResponseHeader.setCampaignEndDate(campaignHeader.getCampaignEndDate());
		campaignResponseHeader.setCampaignActive(campaignHeader.getCampaignActive());
		campaignResponseHeader.setAction(campaignHeader.getAction());
		campaignResponseHeader.setDateTimeStamp(campaignHeader.getDateTimeStamp());
		campaignResponseHeader.setUser(campaignHeader.getUser());
	}

	/*
	 * This method is to set the detail fields in the campaign response history
	 */
	public CampaignResponseHeader save(CampaignResponseHeader campaignResponseHeader,
			List<CampaignRequestDetail> campaignDetails, ResponseDetail responseDetail) {
		List<CampaignResponseDetail> campaignResponseDetails = new ArrayList<>();

		int totalDetails = campaignDetails.size();
		if (totalDetails == 0) {
			CampaignResponseDetail campaignResponseDetail = new CampaignResponseDetail();

			campaignResponseDetail.setChannel(campaignResponseHeader.getChannel());
			if(campaignResponseHeader.getCampaignCode() != null) {
				campaignResponseDetail.setCampaignCode(campaignResponseHeader.getCampaignCode().stripTrailing());
				}else {
					campaignResponseDetail.setCampaignCode(campaignResponseHeader.getCampaignCode());
				}			campaignResponseDetail.setCampaignStartDate(campaignResponseHeader.getCampaignStartDate());
			campaignResponseDetail.setCampaignEndDate(campaignResponseHeader.getCampaignEndDate());
			campaignResponseDetail.setCampaignActive(campaignResponseHeader.getCampaignActive());
			campaignResponseDetail.setAction(campaignResponseHeader.getAction());
			campaignResponseDetail.setDateTimeStamp(campaignResponseHeader.getDateTimeStamp());
			campaignResponseDetail.setUser(campaignResponseHeader.getUser());

			campaignResponseDetail.setWarehouse(" ");
			campaignResponseDetail.setSkuBarcode(" ");

			campaignResponseDetails.add(campaignResponseDetail);
		}
		for (CampaignRequestDetail campaignDetail : campaignDetails) {
			CampaignResponseDetail campaignResponseDetail = new CampaignResponseDetail();

			if(campaignDetail.getCompany() != null) {
			campaignResponseDetail.setCompany(campaignDetail.getCompany().stripTrailing());
			}else {
				campaignResponseDetail.setCompany(campaignDetail.getCompany());
			}
			
			if(campaignDetail.getDivision() != null) {
				campaignResponseDetail.setDivision(campaignDetail.getDivision().stripTrailing());
				}else {
					campaignResponseDetail.setDivision(campaignDetail.getDivision());
				}
			
			if(campaignDetail.getWarehouse() != null) {
				campaignResponseDetail.setWarehouse(campaignDetail.getWarehouse().stripTrailing());
				}else {
					campaignResponseDetail.setWarehouse(campaignDetail.getWarehouse());
				}
			campaignResponseDetail.setSkuBarcode(campaignDetail.getSkuBarcode());
			campaignResponseDetail.setSeason(campaignDetail.getSeason());
			campaignResponseDetail.setSeasonYear(campaignDetail.getSeasonYear());
			campaignResponseDetail.setStyle(campaignDetail.getStyle());
			campaignResponseDetail.setStyleSfx(campaignDetail.getStyleSfx());
			campaignResponseDetail.setColor(campaignDetail.getColor());
			campaignResponseDetail.setColorSfx(campaignDetail.getColorSfx());
			campaignResponseDetail.setSecDimension(campaignDetail.getSecDimension());
			campaignResponseDetail.setQuality(campaignDetail.getQuality());
			campaignResponseDetail.setSizeRngeCode(campaignDetail.getSizeRngeCode());
			campaignResponseDetail.setSizeRelPosnIn(campaignDetail.getSizeRelPosnIn());
			campaignResponseDetail.setInventoryType(campaignDetail.getInventoryType());
			campaignResponseDetail.setLotNumber(campaignDetail.getLotNumber());
			campaignResponseDetail.setCountryOfOrigin(campaignDetail.getCountryOfOrigin());
			campaignResponseDetail.setProductStatus(campaignDetail.getProductStatus());
			campaignResponseDetail.setSkuAttribute1(campaignDetail.getSkuAttribute1());
			campaignResponseDetail.setSkuAttribute2(campaignDetail.getSkuAttribute2());
			campaignResponseDetail.setSkuAttribute3(campaignDetail.getSkuAttribute3());
			campaignResponseDetail.setSkuAttribute4(campaignDetail.getSkuAttribute4());
			campaignResponseDetail.setSkuAttribute5(campaignDetail.getSkuAttribute5());
			campaignResponseDetail.setMinQty(campaignDetail.getMinQty());
			campaignResponseDetail.setMaxQty(campaignDetail.getMaxQty());
			campaignResponseDetail.setProtectQty(campaignDetail.getProtectQty());
			campaignResponseDetail.setAutoReplenish(campaignDetail.getAutoReplenish());

			if (campaignResponseHeader.getAction() == null) {
				campaignResponseDetail.setResponseDetail(responseDetail);
			} else {
				if (!((campaignResponseHeader.getAction().equalsIgnoreCase(createAction))
						|| (campaignResponseHeader.getAction().equalsIgnoreCase(updateAction)))) {
					campaignResponseDetail.setResponseDetail(responseDetail);
				} else {
					campaignResponseDetail.setResponseDetail(campaignDetail.getResponseDetail());
				}
			}
			campaignResponseDetail.setChannel(campaignResponseHeader.getChannel());
			
			if(campaignResponseHeader.getCampaignCode() != null) {
				campaignResponseDetail.setCampaignCode(campaignResponseHeader.getCampaignCode().stripTrailing());
				}else {
					campaignResponseDetail.setCampaignCode(campaignResponseHeader.getCampaignCode());
				}	
			campaignResponseDetail.setCampaignStartDate(campaignResponseHeader.getCampaignStartDate());
			campaignResponseDetail.setCampaignEndDate(campaignResponseHeader.getCampaignEndDate());
			campaignResponseDetail.setCampaignActive(campaignResponseHeader.getCampaignActive());
			campaignResponseDetail.setAction(campaignResponseHeader.getAction());
			campaignResponseDetail.setDateTimeStamp(campaignResponseHeader.getDateTimeStamp());
			campaignResponseDetail.setUser(campaignResponseHeader.getUser());
			campaignResponseDetail.setTransactionNumber(campaignResponseHeader.getTransactionNumber());

			campaignResponseDetails.add(campaignResponseDetail);
		
		}
		campaignResponseHeader.setCampaignResponseDetails(campaignResponseDetails);
		return campaignResponseHeaderRepository.save(campaignResponseHeader);
	}
	
	/*
	 * This method is to save the existing details in history logs , without specifying SKUs in JSON in the update request
	 */
	public CampaignResponseHeader saveDetailDb(CampaignResponseHeader campaignResponseHeader,
			List<CampaignDetailDb> campaignDetails, ResponseDetail responseDetail) {
		List<CampaignResponseDetail> campaignResponseDetails = new ArrayList<>();

		for (CampaignDetailDb campaignDetailDb : campaignDetails) {
			CampaignResponseDetail campaignResponseDetail = new CampaignResponseDetail();

			campaignResponseDetail.setCompany(campaignDetailDb.getId().getSkuInventoryKey().getCompany());
			campaignResponseDetail.setDivision(campaignDetailDb.getId().getSkuInventoryKey().getDivision());
			campaignResponseDetail.setWarehouse(campaignDetailDb.getId().getSkuInventoryKey().getWarehouse());
			campaignResponseDetail.setSkuBarcode(campaignDetailDb.getId().getSkuInventoryKey().getSkuBarcode());
			campaignResponseDetail.setSeason(campaignDetailDb.getId().getSkuInventoryKey().getSeason());
			campaignResponseDetail.setSeasonYear(campaignDetailDb.getId().getSkuInventoryKey().getSeasonYear());
			campaignResponseDetail.setStyle(campaignDetailDb.getId().getSkuInventoryKey().getStyle());
			campaignResponseDetail.setStyleSfx(campaignDetailDb.getId().getSkuInventoryKey().getStyleSfx());
			campaignResponseDetail.setColor(campaignDetailDb.getId().getSkuInventoryKey().getColor());
			campaignResponseDetail.setColorSfx(campaignDetailDb.getId().getSkuInventoryKey().getColorSfx());
			campaignResponseDetail.setSecDimension(campaignDetailDb.getId().getSkuInventoryKey().getSecDimension());
			campaignResponseDetail.setQuality(campaignDetailDb.getId().getSkuInventoryKey().getQuality());
			campaignResponseDetail.setSizeRngeCode(campaignDetailDb.getId().getSkuInventoryKey().getSizeRngeCode());
			campaignResponseDetail.setSizeRelPosnIn(campaignDetailDb.getId().getSkuInventoryKey().getSizeRelPosnIn());
			campaignResponseDetail.setInventoryType(campaignDetailDb.getId().getSkuInventoryKey().getInventoryType());
			campaignResponseDetail.setLotNumber(campaignDetailDb.getId().getSkuInventoryKey().getLotNumber());
			campaignResponseDetail
					.setCountryOfOrigin(campaignDetailDb.getId().getSkuInventoryKey().getCountryOfOrigin());
			campaignResponseDetail.setProductStatus(campaignDetailDb.getId().getSkuInventoryKey().getProductStatus());
			campaignResponseDetail.setSkuAttribute1(campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute1());
			campaignResponseDetail.setSkuAttribute2(campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute2());
			campaignResponseDetail.setSkuAttribute3(campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute3());
			campaignResponseDetail.setSkuAttribute4(campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute4());
			campaignResponseDetail.setSkuAttribute5(campaignDetailDb.getId().getSkuInventoryKey().getSkuAttribute5());
			campaignResponseDetail.setMinQty(campaignDetailDb.getMinimumQuantity());
			campaignResponseDetail.setMaxQty(campaignDetailDb.getMaximumQuantity());
			campaignResponseDetail.setProtectQty(campaignDetailDb.getOriginalProtectQuantity());
			campaignResponseDetail.setAutoReplenish(campaignDetailDb.getAutoReplenish());
			campaignResponseDetail.setResponseDetail(responseDetail);

			campaignResponseDetail.setChannel(campaignResponseHeader.getChannel());
			campaignResponseDetail.setCampaignCode(campaignResponseHeader.getCampaignCode());
			campaignResponseDetail.setCampaignStartDate(campaignResponseHeader.getCampaignStartDate());
			campaignResponseDetail.setCampaignEndDate(campaignResponseHeader.getCampaignEndDate());
			campaignResponseDetail.setCampaignActive(campaignResponseHeader.getCampaignActive());
			campaignResponseDetail.setAction(campaignResponseHeader.getAction());
			campaignResponseDetail.setDateTimeStamp(campaignResponseHeader.getDateTimeStamp());
			campaignResponseDetail.setUser(campaignResponseHeader.getUser());
			campaignResponseDetail.setTransactionNumber(campaignResponseHeader.getTransactionNumber());

			campaignResponseDetails.add(campaignResponseDetail);
		}
		campaignResponseHeader.setCampaignResponseDetails(campaignResponseDetails);
		return campaignResponseHeaderRepository.save(campaignResponseHeader);
	}
}