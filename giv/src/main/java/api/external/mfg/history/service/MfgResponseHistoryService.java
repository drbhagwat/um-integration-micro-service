package api.external.mfg.history.service;

import api.external.mfg.history.entity.MfgHistorySearchCriteria;
import api.external.mfg.history.entity.MfgResponseHistorySku;
import api.external.mfg.history.repo.MfgResponseHistorySkuRepository;
import api.external.util.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class is used to log request of the MfgInventory API call, and implements the
 * CR(Create and Read) services of MfgResponseHistoryHeader and MfgResponseHistorySku.
 * 
 * @author : Thamilarasi
 * @date : 2019-11-01
 */
@Service
public class MfgResponseHistoryService {
	
	@Autowired
	private MfgResponseHistorySkuRepository mfgResponseHistorySkuRepository;

	/*
	 * Get all ResponseHistory of the MfgInventory API by transaction number
	 */
	public Page<MfgResponseHistorySku> getAllResponseHistorySkuByTransactionNumber(String transactionNumber,
			Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return mfgResponseHistorySkuRepository.findByTransactionNumberIgnoreCaseContaining(transactionNumber, paging);
	}

	/*
	 * Get all ResponseHistory of the MfgInventory API
	 */
	public Page<MfgResponseHistorySku> getAllResponseHistory(Integer pageNo, Integer pageSize, String sortBy,
			String orderBy) {
		Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
				: PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		return mfgResponseHistorySkuRepository.findAll(paging);
	}

	/**
	 * Performs search operations in MfgResponseHistories based on the search parameters.
	 * 
	 * @param searchCriteria - Json class to represents search parameters
	 * @param pageNo         - pageNo to display. Default is 0, can be overridden by
	 *                       caller.
	 * @param pageSize       - pageSize to display. Default is 10, can be overridden
	 *                       by caller.
	 * @param orderBy        - orderBy - Default is descending, can be overridden by
	 *                       caller.
	 * @return - on success, returns a page of mfgResponseHistories (could be
	 *         empty). Otherwise, a global rest exception handler is automatically
	 *         called and a context-sensitive error message is displayed.
	 */
	public Page<MfgResponseHistorySku> searchMfgRequestHistory(MfgHistorySearchCriteria searchCriteria, Integer pageNo,
			Integer pageSize, String orderBy) {
		String transactionNumber = searchCriteria.getTransactionNumber();
		String manufacturingPlantCode = searchCriteria.getManufacturingPlantCode();
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
			transactionNumber = transactionNumber.trim().toUpperCase();
		}

		if ((manufacturingPlantCode == null) || manufacturingPlantCode.equals("*") || manufacturingPlantCode.equals("")) {
			manufacturingPlantCode = "";
		} else {
			manufacturingPlantCode = manufacturingPlantCode.trim().toUpperCase();
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
		return mfgResponseHistorySkuRepository
				.findByTransactionNumberIgnoreCaseContainingAndManufacturingPlantCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
						paging, transactionNumber, manufacturingPlantCode, skuBarcode, ldtUserSuppliedStartDate, ldtUserSuppliedEndDate);
	}
}