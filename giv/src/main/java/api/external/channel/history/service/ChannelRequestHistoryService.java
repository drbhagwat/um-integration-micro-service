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

import api.external.util.Converter;
import api.external.channel.entity.ChannelAPI;
import api.external.channel.entity.ChannelAPISku;
import api.external.channel.history.entity.ChannelRequestHistoryHeader;
import api.external.channel.history.entity.ChannelRequestHistorySku;
import api.external.channel.history.entity.HistorySearchCriteria;
import api.external.channel.history.repo.ChannelRequestHistoryHeaderRepository;
import api.external.channel.history.repo.ChannelRequestHistorySkuRepository;

/**
 * This class is used to log request of the channelAPI call, and implements the
 * CR(Create and Read) services of ChannelRequestHistoryHeader and ChannelRequestHistorySku.
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-18
 *
 */
@Service
@Transactional
public class ChannelRequestHistoryService {
	@Autowired
	private ChannelRequestHistoryHeaderRepository channelRequestHistoryHeaderRepository;

	@Autowired
	private ChannelRequestHistorySkuRepository channelRequestHistorySkuRepository;

	/**
	 * Retrieves channelRequestHistories by the transactionNumber.
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
	 * @return - on success, returns a page of channelRequestHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<ChannelRequestHistorySku> getAllRequestHistorySkuByTransactionNumber(String transactionNumber,
			Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelRequestHistorySkuRepository.findByTransactionNumberIgnoreCaseContaining(transactionNumber,
				paging);
	}

	/**
	 * 
	 * Retrieves all channelRequestHistories of a specific channel.
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
	 * @return - on success, returns a page of channelRequestHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<ChannelRequestHistorySku> getAllRequestHistorySkuByChannel(String channel, Integer pageNo,
			Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelRequestHistorySkuRepository.findByChannelIgnoreCaseContaining(channel, paging);
	}

	/**
	 * Saves channelRequestHistoryHeader and channelRequestHistorySku.
	 * 
	 * @param channelAPI - channelAPI (i.e. Json payload/request), which you want to
	 *                   store in the request history
	 * @return - on success, history of the channelAPI gets added. Otherwise, a
	 *         global rest exception handler is automatically called and a
	 *         context-sensitive error message is displayed.
	 */
	public ChannelRequestHistoryHeader save(ChannelAPI channelAPI) {
		ChannelRequestHistoryHeader channelAPIRequestHistoryHeader = new ChannelRequestHistoryHeader();

		channelAPIRequestHistoryHeader.setTransactionNumber(channelAPI.getTransactionNumber());
		channelAPIRequestHistoryHeader.setChannel(channelAPI.getChannel());
		channelAPIRequestHistoryHeader.setCampaignCode(channelAPI.getCampaignCode());
		channelAPIRequestHistoryHeader.setDateTimeStamp(channelAPI.getDateTimeStamp());
		channelAPIRequestHistoryHeader.setUser(channelAPI.getUser());

		List<ChannelRequestHistorySku> channelAPIRequestHistorySkus = new ArrayList<>();

		List<ChannelAPISku> channelAPISkus = channelAPI.getMultipleSkus();

		for (ChannelAPISku sku : channelAPISkus) {
			ChannelRequestHistorySku channelAPIRequestHistorySku = new ChannelRequestHistorySku();

			channelAPIRequestHistorySku.setCompany(sku.getCompany());
			channelAPIRequestHistorySku.setDivision(sku.getDivision());
			channelAPIRequestHistorySku.setWarehouse(sku.getWarehouse());
			channelAPIRequestHistorySku.setManufacturingPlantCode(sku.getManufacturingPlantCode());
			channelAPIRequestHistorySku.setSkuBarcode(sku.getSkuBarcode());
			channelAPIRequestHistorySku.setSeason(sku.getSeason());
			channelAPIRequestHistorySku.setSeasonYear(sku.getSeasonYear());
			channelAPIRequestHistorySku.setStyle(sku.getStyle());
			channelAPIRequestHistorySku.setStyleSfx(sku.getStyleSfx());
			channelAPIRequestHistorySku.setColor(sku.getColor());
			channelAPIRequestHistorySku.setColorSfx(sku.getColorSfx());
			channelAPIRequestHistorySku.setSecDimension(sku.getSecDimension());
			channelAPIRequestHistorySku.setQuality(sku.getQuality());
			channelAPIRequestHistorySku.setSizeRngeCode(sku.getSizeRngeCode());
			channelAPIRequestHistorySku.setSizeRelPosnIn(sku.getSizeRelPosnIn());
			channelAPIRequestHistorySku.setInventoryType(sku.getInventoryType());
			channelAPIRequestHistorySku.setLotNumber(sku.getLotNumber());
			channelAPIRequestHistorySku.setCountryOfOrigin(sku.getCountryOfOrigin());
			channelAPIRequestHistorySku.setProductStatus(sku.getProductStatus());
			channelAPIRequestHistorySku.setSkuAttribute1(sku.getSkuAttribute1());
			channelAPIRequestHistorySku.setSkuAttribute2(sku.getSkuAttribute2());
			channelAPIRequestHistorySku.setSkuAttribute3(sku.getSkuAttribute3());
			channelAPIRequestHistorySku.setSkuAttribute4(sku.getSkuAttribute4());
			channelAPIRequestHistorySku.setSkuAttribute5(sku.getSkuAttribute5());
			channelAPIRequestHistorySku.setSalesOrderNumber(sku.getSalesOrderNumber());
			channelAPIRequestHistorySku.setOrderType(sku.getOrderType());
			channelAPIRequestHistorySku.setShipDate(sku.getShipDate());
			channelAPIRequestHistorySku.setInventorySource(sku.getInventorySource());
			channelAPIRequestHistorySku.setQty(sku.getQty());
			channelAPIRequestHistorySku.setAction(sku.getAction());
			channelAPIRequestHistorySku.setCampaignCode(channelAPI.getCampaignCode());
			channelAPIRequestHistorySku.setChannel(channelAPI.getChannel());
			channelAPIRequestHistorySku.setDateTimeStamp(channelAPI.getDateTimeStamp());
			channelAPIRequestHistorySku.setUser(channelAPI.getUser());

			channelAPIRequestHistorySkus.add(channelAPIRequestHistorySku);
		}
		channelAPIRequestHistoryHeader.setMultipleSkus(channelAPIRequestHistorySkus);
		return channelRequestHistoryHeaderRepository.save(channelAPIRequestHistoryHeader);
	}

	/**
	 * Performs search operations in channelRequestHistories of a specific channel
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
	public Page<ChannelRequestHistorySku> searchChannelRequestHistory(String channel,
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
			// the start of the day with time (00:00)
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
			// the day with time (23:59:59.9999999)
			ldtUserSuppliedEndDate = ldateUserSuppledEndDate.atTime(LocalTime.MAX);
		}

		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return channelRequestHistorySkuRepository
				.findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, division, warehouse, channel, skuBarcode, ldtUserSuppliedStartDate,
						ldtUserSuppliedEndDate);
	}
}