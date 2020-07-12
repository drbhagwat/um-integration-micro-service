package api.external.mfg.history.service;

import api.external.mfg.entity.MfgInvRequest;
import api.external.mfg.entity.MfgInvSku;
import api.external.mfg.history.entity.MfgHistorySearchCriteria;
import api.external.mfg.history.entity.MfgRequestHistoryHeader;
import api.external.mfg.history.entity.MfgRequestHistorySku;
import api.external.mfg.history.repo.MfgRequestHistoryHeaderRepository;
import api.external.mfg.history.repo.MfgRequestHistorySkuRepository;
import api.external.util.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to log request of the MfgInventory API call, and implements the
 * CR(Create and Read) services of MfgRequestHistoryHeader and MfgRequestHistorySku.
 * 
 * @author : Thamilarasi
 * @date : 2019-11-01
 */
@Service
public class MfgRequestHistoryService {

	@Autowired
	private MfgRequestHistoryHeaderRepository mfgRequestHistoryHeaderRepository;

	@Autowired
	private MfgRequestHistorySkuRepository mfgRequestHistorySkuRepository;

	/*
	 * Get all requestHistory of the MfgInventory API by transaction number
	 */
	public Page<MfgRequestHistorySku> getAllRequestHistorySkuByTransactionNumber(String transactionNumber,
			Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return mfgRequestHistorySkuRepository.findByTransactionNumberIgnoreCaseContaining(transactionNumber, paging);
	}

	/*
	 * Get all requestHistory of the MfgInventory API
	 */
	public Page<MfgRequestHistorySku> getAllRequestHistory(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return mfgRequestHistorySkuRepository.findAll(paging);
	}

	/**
	 * Performs search operations in MfgRequestHistories based on the search parameters.
	 * 
	 * @param searchCriteria - Json class to represents search parameters
	 * @param pageNo         - pageNo to display. Default is 0, can be overridden by
	 *                       caller.
	 * @param pageSize       - pageSize to display. Default is 10, can be overridden
	 *                       by caller.
	 * @param orderBy        - orderBy - Default is descending, can be overridden by
	 *                       caller.
	 * @return - on success, returns a page of mfgRequestHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<MfgRequestHistorySku> searchMfgRequestHistory(MfgHistorySearchCriteria searchCriteria, Integer pageNo,
			Integer pageSize, String orderBy) {
		String transactionNumber = searchCriteria.getTransactionNumber();
		String manufacturingPlant = searchCriteria.getManufacturingPlantCode();
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

		if ((manufacturingPlant == null) || manufacturingPlant.equals("*") || manufacturingPlant.equals("")) {
			manufacturingPlant = "";
		} else {
			manufacturingPlant = manufacturingPlant.trim();
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
			// the day with time (23:59:59.9999999)
			ldtUserSuppliedEndDate = ldateUserSuppledEndDate.atTime(LocalTime.MAX);
		}

		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return mfgRequestHistorySkuRepository
				.findByTransactionNumberIgnoreCaseContainingAndManufacturingPlantCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, manufacturingPlant, skuBarcode, ldtUserSuppliedStartDate,
						ldtUserSuppliedEndDate);
	}

	/*
	 * Saves the MfgRequestHistoryHeader and MfgRequestHistorySku
	 */
	public MfgRequestHistoryHeader save(MfgInvRequest mfgInv) {
		MfgRequestHistoryHeader mfgRequestHistoryHeader = new MfgRequestHistoryHeader();
		List<MfgRequestHistorySku> mfgRequestHistorySkus = new ArrayList<>();

		mfgRequestHistoryHeader.setTransactionNumber(mfgInv.getTransactionNumber());
		mfgRequestHistoryHeader.setDateTimeStamp(mfgInv.getDateTimeStamp());
		mfgRequestHistoryHeader.setUser(mfgInv.getUser());
		mfgRequestHistoryHeader.setProductionOrderNumber(mfgInv.getProductionOrderNumber());
		mfgRequestHistoryHeader.setPurchaseOrderNumber(mfgInv.getPurchaseOrderNumber());
		mfgRequestHistoryHeader.setCustomerOrderNumber(mfgInv.getCustomerOrderNumber());

		List<MfgInvSku> mfgInvSkus = mfgInv.getMultipleSkus();

		for (MfgInvSku mfgInvSku : mfgInvSkus) {
			MfgRequestHistorySku mfgRequestHistorySku = new MfgRequestHistorySku();

			mfgRequestHistorySku.setCompany(mfgInvSku.getCompany());
			mfgRequestHistorySku.setDivision(mfgInvSku.getDivision());
			mfgRequestHistorySku.setManufacturingPlantCode(mfgInvSku.getManufacturingPlantCode());
			mfgRequestHistorySku.setSkuBarcode(mfgInvSku.getSkuBarcode());
			mfgRequestHistorySku.setSeason(mfgInvSku.getSeason());
			mfgRequestHistorySku.setSeasonYear(mfgInvSku.getSeasonYear());
			mfgRequestHistorySku.setStyle(mfgInvSku.getStyle());
			mfgRequestHistorySku.setStyleSfx(mfgInvSku.getStyleSfx());
			mfgRequestHistorySku.setColor(mfgInvSku.getColor());
			mfgRequestHistorySku.setColorSfx(mfgInvSku.getColorSfx());
			mfgRequestHistorySku.setSecDimension(mfgInvSku.getSecDimension());
			mfgRequestHistorySku.setQuality(mfgInvSku.getQuality());
			mfgRequestHistorySku.setSizeRngeCode(mfgInvSku.getSizeRngeCode());
			mfgRequestHistorySku.setSizeRelPosnIn(mfgInvSku.getSizeRelPosnIn());
			mfgRequestHistorySku.setInventoryType(mfgInvSku.getInventoryType());
			mfgRequestHistorySku.setLotNumber(mfgInvSku.getLotNumber());
			mfgRequestHistorySku.setCountryOfOrigin(mfgInvSku.getCountryOfOrigin());
			mfgRequestHistorySku.setProductStatus(mfgInvSku.getProductStatus());
			mfgRequestHistorySku.setSkuAttribute1(mfgInvSku.getSkuAttribute1());
			mfgRequestHistorySku.setSkuAttribute2(mfgInvSku.getSkuAttribute2());
			mfgRequestHistorySku.setSkuAttribute3(mfgInvSku.getSkuAttribute3());
			mfgRequestHistorySku.setSkuAttribute4(mfgInvSku.getSkuAttribute4());
			mfgRequestHistorySku.setSkuAttribute5(mfgInvSku.getSkuAttribute5());

			mfgRequestHistorySku.setSerialNumber(mfgInvSku.getSerialNumber());
			mfgRequestHistorySku.setReasonCode(mfgInvSku.getReasonCode());
			mfgRequestHistorySku.setQty(mfgInvSku.getQty());
			mfgRequestHistorySku.setDesc(mfgInvSku.getDesc());
			mfgRequestHistorySku.setAction(mfgInvSku.getAction());
			mfgRequestHistorySku.setInventorySource(mfgInvSku.getInventorySource());

			mfgRequestHistorySku.setDateTimeStamp(mfgInv.getDateTimeStamp());
			mfgRequestHistorySku.setUser(mfgInv.getUser());
			mfgRequestHistorySku.setProductionOrderNumber(mfgInv.getProductionOrderNumber());
			mfgRequestHistorySku.setCustomerOrderNumber(mfgInv.getCustomerOrderNumber());
			mfgRequestHistorySku.setPurchaseOrderNumber(mfgInv.getPurchaseOrderNumber());

			mfgRequestHistorySkus.add(mfgRequestHistorySku);
		}
		mfgRequestHistoryHeader.setMultipleSkus(mfgRequestHistorySkus);
		return mfgRequestHistoryHeaderRepository.save(mfgRequestHistoryHeader);
	}
}