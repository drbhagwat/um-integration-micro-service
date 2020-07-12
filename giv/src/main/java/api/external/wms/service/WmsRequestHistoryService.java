package api.external.wms.service;

import api.external.inventory.entity.SKUInventoryKey;
import api.external.util.Converter;
import api.external.wms.entity.WmsInvRequest;
import api.external.wms.entity.WmsRequestDetail;
import api.external.wms.entity.WmsRequestHeader;
import api.external.wms.entity.WmsSku;
import api.external.wms.repo.WmsRequestDetailRepository;
import api.external.wms.repo.WmsRequestHeaderRepository;
import api.external.wms.search.entity.WmsHistorySearchCriteria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * This class is used to log request of an wms inventory api call.
 * @author : Sachin Kulkarni
 * @date : 18-10-2019
 */
@Service
public class WmsRequestHistoryService {
  @Autowired
  private WmsRequestHeaderRepository wmsRequestHeaderRepository;

  @Autowired
  private WmsRequestDetailRepository wmsRequestDetailRepository;

  // This method is used to save the request
  public WmsRequestHeader save(WmsInvRequest wmsInvRequest) {
    WmsRequestHeader wmsRequestHeader = new WmsRequestHeader();
    List<WmsRequestDetail> wmsRequestDetails = new ArrayList<>();

    wmsRequestHeader.setTransactionNumber(wmsInvRequest.getTransactionNumber());
    wmsRequestHeader.setDateTimeStamp(wmsInvRequest.getDateTimeStamp());
    wmsRequestHeader.setUser(wmsInvRequest.getUser());

    List<WmsSku> wmsSkuList = wmsInvRequest.getMultipleSkus();

    for (WmsSku wmsSku : wmsSkuList) {
      WmsRequestDetail wmsRequestDetail = new WmsRequestDetail();
      SKUInventoryKey skuInventoryKey = wmsSku.getId();

      wmsRequestDetail.setSerialNumber(wmsSku.getSerialNumber());
      wmsRequestDetail.setReasonCode(wmsSku.getReasonCode());
      wmsRequestDetail.setQty(wmsSku.getQty());
      wmsRequestDetail.setDesc(wmsSku.getDesc());
      wmsRequestDetail.setAction(wmsSku.getAction());
      wmsRequestDetail.setInventorySource(wmsSku.getInventorySource());
      wmsRequestDetail.setCompany(skuInventoryKey.getCompany());
      wmsRequestDetail.setDivision(skuInventoryKey.getDivision());
      wmsRequestDetail.setWarehouse(skuInventoryKey.getWarehouse());
      wmsRequestDetail.setSkuBarcode(skuInventoryKey.getSkuBarcode());
      wmsRequestDetail.setSeason(skuInventoryKey.getSeason());
      wmsRequestDetail.setSeasonYear(skuInventoryKey.getSeasonYear());
      wmsRequestDetail.setStyle(skuInventoryKey.getStyle());
      wmsRequestDetail.setStyleSfx(skuInventoryKey.getStyleSfx());
      wmsRequestDetail.setColor(skuInventoryKey.getColor());
      wmsRequestDetail.setColorSfx(skuInventoryKey.getColorSfx());
      wmsRequestDetail.setSecDimension(skuInventoryKey.getSecDimension());
      wmsRequestDetail.setQuality(skuInventoryKey.getQuality());
      wmsRequestDetail.setSizeRngeCode(skuInventoryKey.getSizeRngeCode());
      wmsRequestDetail.setSizeRelPosnIn(skuInventoryKey.getSizeRelPosnIn());
      wmsRequestDetail.setInventoryType(skuInventoryKey.getInventoryType());
      wmsRequestDetail.setLotNumber(skuInventoryKey.getLotNumber());
      wmsRequestDetail.setCountryOfOrigin(skuInventoryKey.getCountryOfOrigin());
      wmsRequestDetail.setProductStatus(skuInventoryKey.getProductStatus());
      wmsRequestDetail.setSkuAttribute1(skuInventoryKey.getSkuAttribute1());
      wmsRequestDetail.setSkuAttribute2(skuInventoryKey.getSkuAttribute2());
      wmsRequestDetail.setSkuAttribute3(skuInventoryKey.getSkuAttribute3());
      wmsRequestDetail.setSkuAttribute4(skuInventoryKey.getSkuAttribute4());
      wmsRequestDetail.setSkuAttribute5(skuInventoryKey.getSkuAttribute5());
      wmsRequestDetail.setTransactionNumber(wmsInvRequest.getTransactionNumber());
      wmsRequestDetail.setUser(wmsInvRequest.getUser());
      wmsRequestDetail.setDateTimeStamp(wmsInvRequest.getDateTimeStamp());
      wmsRequestDetails.add(wmsRequestDetail);
    }
    wmsRequestHeader.setWmsRequestDetails(wmsRequestDetails);
    return wmsRequestHeaderRepository.save(wmsRequestHeader);
  }

  // This method is used to search the request history
  public Page<WmsRequestDetail> searchWmsRequestHistory(WmsHistorySearchCriteria wmsHistorySearchCriteria,
                                                        Integer pageNo, Integer pageSize, String orderBy) {
    // initialize the search criteria fields
    String transactionNumber = wmsHistorySearchCriteria.getTransactionNumber();
    String division = wmsHistorySearchCriteria.getDivision();
    String warehouse = wmsHistorySearchCriteria.getWarehouse();
    String skuBarcode = wmsHistorySearchCriteria.getSkuBarcode();
    String sortBy = wmsHistorySearchCriteria.getSortBy();

    // handle both wild card and when the search parameter is not present in
    // campaign search criteria
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

    Page<WmsRequestDetail> details = wmsRequestDetailRepository
        .findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
            paging, transactionNumber, division, warehouse, skuBarcode, ldtUserSuppliedStartDate,
            ldtUserSuppliedEndDate);

    return details;
  }

  // This method is used to get all the history of the request.
  public Page<WmsRequestDetail> getAll(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    Pageable pageable = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
            : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

    return wmsRequestDetailRepository.getAll(pageable);
  }
}
