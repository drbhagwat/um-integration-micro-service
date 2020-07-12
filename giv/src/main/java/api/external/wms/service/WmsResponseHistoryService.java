package api.external.wms.service;

import api.external.util.Converter;
import api.external.wms.entity.WmsResponseDetail;
import api.external.wms.repo.WmsResponseDetailRepository;
import api.external.wms.search.entity.WmsHistorySearchCriteria;
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
 * This class is used to log response of an wms inventory api call.
 * @author : Sachin Kulkarni
 * @date : 11-10-2019
 */

@Service
public class WmsResponseHistoryService {
  @Autowired
  private WmsResponseDetailRepository wmsResponseDetailRepository;

  // This method is used to search response history log.
  public Page<WmsResponseDetail> searchWmsResponseHistory(WmsHistorySearchCriteria wmsHistorySearchCriteria,
                                                          Integer pageNo, Integer pageSize, String orderBy) {
    // initialize the search criteria fields
    String transactionNumber = wmsHistorySearchCriteria.getTransactionNumber();
    String division = wmsHistorySearchCriteria.getDivision();
    String warehouse = wmsHistorySearchCriteria.getWarehouse();
    String skuBarcode = wmsHistorySearchCriteria.getSkuBarcode();
    String sortBy = wmsHistorySearchCriteria.getSortBy();

    // handle both wild card and when the search parameter is not present in wms search criteria
    if ((transactionNumber == null) || transactionNumber.equals("*")) {
      transactionNumber = "";
    } else {
      transactionNumber = transactionNumber.trim();
    }

    if ((division == null) || division.equals("*")) {
      division = "";
    } else {
      division = division.trim().toUpperCase();
    }

    if ((warehouse == null) || warehouse.equals("*")) {
      warehouse = "";
    } else {
      warehouse = warehouse.trim().toUpperCase();
    }

    // handle both wild card and when the search parameter is not present in search criteria
    if ((skuBarcode == null) || (skuBarcode.equals("*"))) {
      skuBarcode = "";
    } else {
      skuBarcode = skuBarcode.trim().toUpperCase();
    }

    if ((sortBy == null) || (sortBy.equals("*")) || (sortBy.trim().equals(""))) {
      sortBy = "lastUpdatedDateTime";
    } else { // convert the user supplied JSON parameter to equivalent db parameter
      sortBy = Converter.toDatabaseColumnName(sortBy);
    }
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String userSuppliedStartDate = wmsHistorySearchCriteria.getUserSuppliedStartDate();
    LocalDateTime ldtUserSuppliedStartDate = null;
    LocalDate ldateUserSuppledStartDate = null;

    if ((userSuppliedStartDate == null) || userSuppliedStartDate.equals("*") || userSuppliedStartDate.equals("")) {
      ldtUserSuppliedStartDate = LocalDateTime.of(2010,01,1,1,10,10,10000);

    } else {
      ldateUserSuppledStartDate = LocalDate.parse(userSuppliedStartDate, dateTimeFormatter);
      // if the userSuppliedStartDate is given, take the userSuppliedStartDate from
      // the start of the day with time (00:00)
      ldtUserSuppliedStartDate = ldateUserSuppledStartDate.atStartOfDay();
    }

    String userSuppliedEndDate = wmsHistorySearchCriteria.getUserSuppliedEndDate();
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

    Page<WmsResponseDetail> details = wmsResponseDetailRepository
        .findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
            paging, transactionNumber, division, warehouse, skuBarcode, ldtUserSuppliedStartDate,
            ldtUserSuppliedEndDate);

    return details;
  }

  // This method is used to find all response history log.
  public Page<WmsResponseDetail> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
      Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
              : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

      return wmsResponseDetailRepository.getAll(pageable);
    }
}
