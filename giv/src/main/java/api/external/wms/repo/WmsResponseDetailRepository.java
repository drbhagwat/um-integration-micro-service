package api.external.wms.repo;


import api.external.wms.entity.ResponseDetail;
import api.external.wms.entity.WmsResponseDetail;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the item repository for the item response history.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Repository
public interface WmsResponseDetailRepository extends PagingAndSortingRepository<WmsResponseDetail, Long> {
  @Query(value = "SELECT * FROM wms_response_detail", nativeQuery = true)
  Page<WmsResponseDetail> getAll(Pageable pageable);

  @Query(value = "SELECT * FROM wms_response_detail WHERE division LIKE %?1% AND warehouse LIKE %?2% " +
      "AND sku_barcode LIKE %?3%", nativeQuery = true)
  Page<ResponseDetail> search(Pageable pageable, String division, String warehouse,
                              String skuBarcode);

  @Query(value = "SELECT * FROM wms_response_detail WHERE division LIKE %?1% AND warehouse LIKE %?2% " +
      "AND sku_barcode LIKE %?3% AND date_time_stamp >= ?4 AND date_time_stamp < ?5", nativeQuery = true)
  Page<ResponseDetail> search(Pageable pageable, String division, String warehouse,
                              String skuBarcode, Timestamp userSuppliedStartDateStamp,
                              Timestamp userSuppliedEndDateStamp);

  @Query(value = "SELECT * FROM wms_response_detail WHERE division LIKE %?1% AND warehouse LIKE %?2% AND channel " +
      "LIKE %?3% AND sku_barcode LIKE %?4% AND date_time_stamp >= ?5 AND date_time_stamp < ?6", nativeQuery = true)
  Page<ResponseDetail> search(Pageable pageable, String division, String warehouse,
                              String channel, String skuBarcode, Timestamp userSuppliedStartDateStamp,
                              Timestamp userSuppliedEndDateStamp);

  Page<WmsResponseDetail> findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
          Pageable pageable, String transactionNumber, String division, String warehouse, String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);

    WmsResponseDetail findByTransactionNumber(String transactionNumber);
}
