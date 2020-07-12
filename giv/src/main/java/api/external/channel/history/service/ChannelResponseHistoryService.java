
package api.external.channel.history.service;

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

import api.external.channel.entity.ChannelAPI;
import api.external.channel.entity.ChannelAPIResponseSku;
import api.external.channel.history.entity.ChannelResponseHistoryHeader;
import api.external.channel.history.entity.ChannelResponseHistorySku;
import api.external.channel.history.entity.HistorySearchCriteria;
import api.external.channel.history.repo.ChannelResponseHistoryHeaderRepository;
import api.external.channel.history.repo.ChannelResponseHistorySkuRepository;
import api.external.util.Converter;

/**
 * 
 * This class is used to log response of the channelAPI call, and implements the
 * CRUD services of ChannelResponseHistoryHeader and ChannelResponseHistorySku.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-18
 * 
 * 
 *
 *
 */
@Service
@Transactional
public class ChannelResponseHistoryService {

	@Autowired
	private ChannelResponseHistoryHeaderRepository channelResponseHistoryHeaderRepository;

	@Autowired
	private ChannelResponseHistorySkuRepository channelResponseHistorySkuRepository;

	/**
	 * Retrieves channelResponseHistories by the transactionNumber.
	 * 
	 * @param transactionNumber - which is unique for each transaction.
	 * @param pageNo            - pageNo to display. Default is 0, can be overridden
	 *                          by caller.
	 * @param pageSize          - pageSize to display. Default is 10, can be
	 *                          overridden by caller.
	 * @param sortBy            - sortBy which key? Default is lastUpdatedDateTime,
	 *                          can be overridden by caller.
	 * @param orderBy           - orderBy - Default is descending, can be overridden
	 *                          by caller.
	 * @return - on success, returns a page of channelResponseHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<ChannelResponseHistorySku> getAllResponseHistorySkuByTransactionNumber(String transactionNumber,
			Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelResponseHistorySkuRepository.findByTransactionNumberIgnoreCaseContaining(transactionNumber,
				paging);
	}

	/**
	 * 
	 * Retrieves channelResponseHistories of a specific channel
	 * 
	 * @param channel  - code of the channel (i.e. web, catalog, retail)
	 * @param pageNo   - pageNo to display. Default is 0, can be overridden by
	 *                 caller.
	 * @param pageSize - pageSize to display. Default is 10, can be overridden by
	 *                 caller.
	 * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be
	 *                 overridden by caller.
	 * @param orderBy  - orderBy - Default is descending, can be overridden by
	 *                 caller.
	 * @return - on success, returns a page of channelResponseHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<ChannelResponseHistorySku> getAllResponseHistorySkuByChannel(String channel, Integer pageNo,
			Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelResponseHistorySkuRepository.findByChannelIgnoreCaseContaining(channel, paging);
	}

	/**
	 * Performs search operations in channelResponseHistories of a specific channel
	 * based on the search parameters.
	 * 
	 * @param channel        - code of the channel (i.e. web, catalog, retail)
	 * @param searchCriteria - Json class to represents search parameters
	 * @param pageNo         - pageNo to display. Default is 0, can be overridden by
	 *                       caller.
	 * @param pageSize       - pageSize to display. Default is 10, can be overridden
	 *                       by caller.
	 * @param orderBy        - orderBy - Default is descending, can be overridden by
	 *                       caller.
	 * @return - on success, returns a page of channelResponseHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<ChannelResponseHistorySku> searchChannelResponseHistory(String channel,
			HistorySearchCriteria searchCriteria, Integer pageNo, Integer pageSize, String orderBy) {
		String transactionNumber = searchCriteria.getTransactionNumber();
		String division = searchCriteria.getDivision();
		String warehouse = searchCriteria.getWarehouse();
		String skuBarcode = searchCriteria.getSkuBarcode();
		String sortBy = searchCriteria.getSortBy();

		// by default sort by last_updated_date_time
		if ((sortBy == null) || (sortBy.equals("*")) || (sortBy.trim().equals(""))) {
			sortBy = "lastUpdatedDateTime";
		} else { // convert the user supplied JSON parameter to equivalent db parameter
			sortBy = Converter.toDatabaseColumnName(sortBy);
		}

		// handle both wild card and when the search parameter is not present in search
		// criteria

		if ((transactionNumber == null) || transactionNumber.equals("*") || transactionNumber.equals("")) {
			transactionNumber = "";
		} else {
			transactionNumber = transactionNumber.trim();
		}

		if ((division == null) || division.equals("*") || division.equals("")) {
			division = "";
		} else {
			division = division.trim();
		}

		if ((warehouse == null) || warehouse.equals("*") || warehouse.equals("")) {
			warehouse = "";
		} else {
			warehouse = warehouse.trim();
		}

		if ((skuBarcode == null) || skuBarcode.equals("*") || skuBarcode.equals("")) {
			skuBarcode = "";
		} else {
			skuBarcode = skuBarcode.trim();
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String userSuppliedStartDate = searchCriteria.getUserSuppliedStartDate();
		LocalDateTime ldtUserSuppliedStartDate = null;
		LocalDate ldateUserSuppledStartDate = null;

		if ((userSuppliedStartDate == null) || userSuppliedStartDate.equals("*") || userSuppliedStartDate.equals("")) {
			ldtUserSuppliedStartDate = LocalDate.of(2010, 12, 31).atStartOfDay();

		} else {
			ldateUserSuppledStartDate = LocalDate.parse(userSuppliedStartDate, dateTimeFormatter);
			// if the userSuppliedStartDate is given, take the userSuppliedStartDate from
			// the start of the day with time
			// (00:00)
			ldtUserSuppliedStartDate = ldateUserSuppledStartDate.atStartOfDay();
		}

		String userSuppliedEndDate = searchCriteria.getUserSuppliedEndDate();
		LocalDateTime ldtUserSuppliedEndDate = null;
		LocalDate ldateUserSuppledEndDate = null;

		if ((userSuppliedEndDate == null) || userSuppliedEndDate.equals("*") || userSuppliedEndDate.equals("")) {
			ldtUserSuppliedEndDate = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
		} else {
			ldateUserSuppledEndDate = LocalDate.parse(userSuppliedEndDate, dateTimeFormatter);
			// if the userSuppliedEndDate is given, take the userSuppliedEndDate upto end of
			// the day with time (23:59:59
			// .9999999)
			ldtUserSuppliedEndDate = ldateUserSuppledEndDate.atTime(LocalTime.MAX);
		}

		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelResponseHistorySkuRepository
				.findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, division, warehouse, channel, skuBarcode, ldtUserSuppliedStartDate,
						ldtUserSuppliedEndDate);
	}

	/**
	 * Saves channelResponseHistoryHeader and channelResponseHistorySku.
	 * 
	 * @param channelAPIResponseHistoryHeader - stores only header level response
	 *                                        history.
	 * @param channelAPI                      - channelAPI(i.e.
	 *                                        JsonPayload/request), which you want
	 *                                        to store in the response history
	 * @param channelAPIResponseSkus          - Is a Json response skus for sending
	 *                                        response back.
	 * @return - on success, history of the channelAPI gets added. Otherwise, a
	 *         global rest exception handler is automatically called and a
	 *         context-sensitive error message is displayed.
	 */
	public ChannelResponseHistoryHeader save(ChannelResponseHistoryHeader channelAPIResponseHistoryHeader,
			ChannelAPI channelAPI, List<ChannelAPIResponseSku> channelAPIResponseSkus) {
		List<ChannelResponseHistorySku> channelAPIResponseHistorySkus = new ArrayList<>();

		for (ChannelAPIResponseSku channelAPIResponseSku : channelAPIResponseSkus) {
			ChannelResponseHistorySku channelAPIResponseHistorySku = new ChannelResponseHistorySku();

			String company = channelAPIResponseSku.getCompany();
			String division = channelAPIResponseSku.getDivision();
			String warehouse = channelAPIResponseSku.getWarehouse();

			if (company != null) {
				channelAPIResponseHistorySku.setCompany(company.stripTrailing());
			} else {
				channelAPIResponseHistorySku.setCompany(company);
			}

			if (division != null) {
				channelAPIResponseHistorySku.setDivision(division.stripTrailing());
			} else {
				channelAPIResponseHistorySku.setDivision(division);
			}

			if (warehouse != null) {
				channelAPIResponseHistorySku.setWarehouse(warehouse.stripTrailing());
			} else {
				channelAPIResponseHistorySku.setWarehouse(warehouse);
			}

			channelAPIResponseHistorySku.setManufacturingPlantCode(channelAPIResponseSku.getManufacturingPlantCode());
			channelAPIResponseHistorySku.setSkuBarcode(channelAPIResponseSku.getSkuBarcode());
			channelAPIResponseHistorySku.setSeason(channelAPIResponseSku.getSeason());
			channelAPIResponseHistorySku.setSeasonYear(channelAPIResponseSku.getSeasonYear());
			channelAPIResponseHistorySku.setStyle(channelAPIResponseSku.getStyle());
			channelAPIResponseHistorySku.setStyleSfx(channelAPIResponseSku.getStyleSfx());
			channelAPIResponseHistorySku.setColor(channelAPIResponseSku.getColor());
			channelAPIResponseHistorySku.setColorSfx(channelAPIResponseSku.getColorSfx());
			channelAPIResponseHistorySku.setSecDimension(channelAPIResponseSku.getSecDimension());
			channelAPIResponseHistorySku.setQuality(channelAPIResponseSku.getQuality());
			channelAPIResponseHistorySku.setSizeRngeCode(channelAPIResponseSku.getSizeRngeCode());
			channelAPIResponseHistorySku.setSizeRelPosnIn(channelAPIResponseSku.getSizeRelPosnIn());
			channelAPIResponseHistorySku.setInventoryType(channelAPIResponseSku.getInventoryType());
			channelAPIResponseHistorySku.setLotNumber(channelAPIResponseSku.getLotNumber());
			channelAPIResponseHistorySku.setCountryOfOrigin(channelAPIResponseSku.getCountryOfOrigin());
			channelAPIResponseHistorySku.setProductStatus(channelAPIResponseSku.getProductStatus());
			channelAPIResponseHistorySku.setSkuAttribute1(channelAPIResponseSku.getSkuAttribute1());
			channelAPIResponseHistorySku.setSkuAttribute2(channelAPIResponseSku.getSkuAttribute2());
			channelAPIResponseHistorySku.setSkuAttribute3(channelAPIResponseSku.getSkuAttribute3());
			channelAPIResponseHistorySku.setSkuAttribute4(channelAPIResponseSku.getSkuAttribute4());
			channelAPIResponseHistorySku.setSkuAttribute5(channelAPIResponseSku.getSkuAttribute5());
			channelAPIResponseHistorySku.setSalesOrderNumber(channelAPIResponseSku.getSalesOrderNumber());
			channelAPIResponseHistorySku.setOrderType(channelAPIResponseSku.getOrderType());
			channelAPIResponseHistorySku.setShipDate(channelAPIResponseSku.getShipDate());
			channelAPIResponseHistorySku.setInventorySource(channelAPIResponseSku.getInventorySource());
			channelAPIResponseHistorySku.setAction(channelAPIResponseSku.getAction());
			channelAPIResponseHistorySku.setRequestedQty(channelAPIResponseSku.getQty());
			channelAPIResponseHistorySku.setResponseQty(channelAPIResponseSku.getResponseQty());
			channelAPIResponseHistorySku.setOnHandQty(channelAPIResponseSku.getOnHandQty());
			channelAPIResponseHistorySku.setAllocatedQty(channelAPIResponseSku.getAllocatedQty());
			channelAPIResponseHistorySku.setProtectedQty(channelAPIResponseSku.getProtectedQty());
			channelAPIResponseHistorySku.setLockedQty(channelAPIResponseSku.getLockedQty());
			channelAPIResponseHistorySku.setAvailableQty(channelAPIResponseSku.getAvailableQty());

			String campaignCode = channelAPI.getCampaignCode();

			if (campaignCode != null) {
				channelAPIResponseHistorySku.setCampaignCode(campaignCode.stripTrailing());
			} else {
				channelAPIResponseHistorySku.setCampaignCode(campaignCode);
			}

			channelAPIResponseHistorySku.setTransactionNumber(channelAPI.getTransactionNumber());
			channelAPIResponseHistorySku.setChannel(channelAPI.getChannel());
			channelAPIResponseHistorySku.setDateTimeStamp(channelAPI.getDateTimeStamp());
			channelAPIResponseHistorySku.setUser(channelAPI.getUser());

			channelAPIResponseHistorySku.setResponseDetail(channelAPIResponseSku.getChannelAPIResponseDetail());
			channelAPIResponseHistorySkus.add(channelAPIResponseHistorySku);
		}
		channelAPIResponseHistoryHeader.setMultipleSkus(channelAPIResponseHistorySkus);
		return channelResponseHistoryHeaderRepository.save(channelAPIResponseHistoryHeader);
	}
}