package api.external.campaign.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import api.external.campaign.entity.CampaignHistorySearchCriteria;
import api.external.campaign.entity.CampaignRequestDetail;
import api.external.campaign.entity.CampaignRequestHeader;
import api.external.campaign.repo.CampaignRequestDetailRepository;
import api.external.campaign.repo.CampaignRequestHeaderRepository;
import api.external.util.Converter;

/**
 * provides create, read and search services for the CampaignRequestHistory entity.
 * Controller.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */
@Service
@Transactional
public class CampaignRequestHistoryService {

	@Autowired
	private CampaignRequestHeaderRepository campaignRequestHeaderRepository;

	@Autowired
	private CampaignRequestDetailRepository campaignRequestDetailRepository;

	/**
	 * This method is used to get all campaignrequesthistory.
	 *
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return Page<CampaignRequestDetail> - on success, returns a page of campaignrequesthistory (could be empty). Otherwise, a global rest
	  	exception handler is automatically called and a context-sensitive error message is displayed.
	 */

	public Page<CampaignRequestDetail> getAllCampaignRequestHistory(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignRequestDetailRepository.findAll(paging);
	}

	/**
	 *  This method is used to get specific campaignrequesthistory based on transactionNumber
	 *
	 * @param transactionNumber    	- transactionNumber by which you want to find the campaign_request_history in the db
	 * @return campaignrequesthistory - This returns an campaignrequesthistory based on criteria.
	 * @throws Exception - if the campaignrequesthistory does not exists
	 */
	
	public Page<CampaignRequestDetail> getCampaignRequestHistory(String transactionNumber, Integer pageNo,
			Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return campaignRequestDetailRepository.findByTransactionNumber(transactionNumber, paging);
	}

	/**
	 * @param searchCriteria - which you want to find the search fields in the
	 *                       campaign_request_history db.
	 * @param pageNo    - pageNo to display. Default is 0, can be overridden by caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
	 * @return - on success, it returns to search the page of particular
	 *         campaign_request_history record found.
	 * @throws Exception - on failure, an exception is thrown and a meaningful error
	 *                   message is displayed.
	 */
	public Page<CampaignRequestDetail> searchCampaignRequestHistory(
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

		Page<CampaignRequestDetail> details = campaignRequestDetailRepository
				.findByTransactionNumberIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndCampaignCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, warehouse, channel, campaignCode, skuBarcode,
						ldtUserSuppliedStartDate, ldtUserSuppliedEndDate);
		return details;
	}

	/*
	 * This method is to set the header and detail fields in the campaign request history
	 */
	public CampaignRequestHeader save(CampaignRequestHeader campaignHeader) {
		CampaignRequestHeader campaignRequestHeader = new CampaignRequestHeader();

		List<CampaignRequestDetail> campaignRequestDetails = new ArrayList<>();

		campaignRequestHeader.setTransactionNumber(campaignHeader.getTransactionNumber());
		campaignRequestHeader.setChannel(campaignHeader.getChannel());
		campaignRequestHeader.setCampaignCode(campaignHeader.getCampaignCode());
		campaignRequestHeader.setCampaignStartDate(campaignHeader.getCampaignStartDate());
		campaignRequestHeader.setCampaignEndDate(campaignHeader.getCampaignEndDate());
		campaignRequestHeader.setCampaignActive(campaignHeader.getCampaignActive());
		campaignRequestHeader.setAction(campaignHeader.getAction());
		campaignRequestHeader.setDateTimeStamp(campaignHeader.getDateTimeStamp());
		campaignRequestHeader.setUser(campaignHeader.getUser());

		List<CampaignRequestDetail> campaignDetails = campaignHeader.getCampaignDetails();

		int totalDetail = campaignDetails.size();
		if (totalDetail == 0) {
			CampaignRequestDetail campaignRequestDetail = new CampaignRequestDetail();

			campaignRequestDetail.setChannel(campaignHeader.getChannel());
			campaignRequestDetail.setCampaignCode(campaignHeader.getCampaignCode());
			campaignRequestDetail.setCampaignStartDate(campaignHeader.getCampaignStartDate());
			campaignRequestDetail.setCampaignEndDate(campaignHeader.getCampaignEndDate());
			campaignRequestDetail.setCampaignActive(campaignHeader.getCampaignActive());
			campaignRequestDetail.setAction(campaignHeader.getAction());
			campaignRequestDetail.setDateTimeStamp(campaignHeader.getDateTimeStamp());
			campaignRequestDetail.setUser(campaignHeader.getUser());

			campaignRequestDetail.setWarehouse(" ");
			campaignRequestDetail.setSkuBarcode(" ");
			campaignRequestDetails.add(campaignRequestDetail);
		} else {
			for (CampaignRequestDetail campaignDetail : campaignDetails) {
				CampaignRequestDetail campaignRequestDetail = new CampaignRequestDetail();

				campaignRequestDetail.setCompany(campaignDetail.getCompany());
				campaignRequestDetail.setDivision(campaignDetail.getDivision());
				campaignRequestDetail.setWarehouse(campaignDetail.getWarehouse());
				campaignRequestDetail.setSkuBarcode(campaignDetail.getSkuBarcode());
				campaignRequestDetail.setSeason(campaignDetail.getSeason());
				campaignRequestDetail.setSeasonYear(campaignDetail.getSeasonYear());
				campaignRequestDetail.setStyle(campaignDetail.getStyle());
				campaignRequestDetail.setStyleSfx(campaignDetail.getStyleSfx());
				campaignRequestDetail.setColor(campaignDetail.getColor());
				campaignRequestDetail.setColorSfx(campaignDetail.getColorSfx());
				campaignRequestDetail.setSecDimension(campaignDetail.getSecDimension());
				campaignRequestDetail.setQuality(campaignDetail.getQuality());
				campaignRequestDetail.setSizeRngeCode(campaignDetail.getSizeRngeCode());
				campaignRequestDetail.setSizeRelPosnIn(campaignDetail.getSizeRelPosnIn());
				campaignRequestDetail.setInventoryType(campaignDetail.getInventoryType());
				campaignRequestDetail.setLotNumber(campaignDetail.getLotNumber());
				campaignRequestDetail.setCountryOfOrigin(campaignDetail.getCountryOfOrigin());
				campaignRequestDetail.setProductStatus(campaignDetail.getProductStatus());
				campaignRequestDetail.setSkuAttribute1(campaignDetail.getSkuAttribute1());
				campaignRequestDetail.setSkuAttribute2(campaignDetail.getSkuAttribute2());
				campaignRequestDetail.setSkuAttribute3(campaignDetail.getSkuAttribute3());
				campaignRequestDetail.setSkuAttribute4(campaignDetail.getSkuAttribute4());
				campaignRequestDetail.setSkuAttribute5(campaignDetail.getSkuAttribute5());
				campaignRequestDetail.setMinQty(campaignDetail.getMinQty());
				campaignRequestDetail.setMaxQty(campaignDetail.getMaxQty());
				campaignRequestDetail.setProtectQty(campaignDetail.getProtectQty());
				campaignRequestDetail.setAutoReplenish(campaignDetail.getAutoReplenish());

				campaignRequestDetail.setChannel(campaignHeader.getChannel());
				campaignRequestDetail.setCampaignCode(campaignHeader.getCampaignCode());
				campaignRequestDetail.setCampaignStartDate(campaignHeader.getCampaignStartDate());
				campaignRequestDetail.setCampaignEndDate(campaignHeader.getCampaignEndDate());
				campaignRequestDetail.setCampaignActive(campaignHeader.getCampaignActive());
				campaignRequestDetail.setAction(campaignHeader.getAction());
				campaignRequestDetail.setDateTimeStamp(campaignHeader.getDateTimeStamp());
				campaignRequestDetail.setUser(campaignHeader.getUser());

				campaignRequestDetails.add(campaignRequestDetail);
			}
		}
		campaignRequestHeader.setCampaignDetails(campaignRequestDetails);
		return campaignRequestHeaderRepository.save(campaignRequestHeader);
	}
}