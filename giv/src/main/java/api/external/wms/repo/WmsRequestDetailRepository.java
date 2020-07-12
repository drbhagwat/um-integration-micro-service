package api.external.wms.repo;


import api.external.wms.entity.WmsRequestDetail;
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
public interface WmsRequestDetailRepository extends PagingAndSortingRepository<WmsRequestDetail, Long> {
    @Query(value = "SELECT * FROM wms_request_detail", nativeQuery = true)
    Page<WmsRequestDetail> getAll(Pageable pageable);

    Page<WmsRequestDetail> findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(
            Pageable pageable, String transactionNumber, String division, String warehouse, String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);

    WmsRequestDetail findByTransactionNumber(String transactionNumber);
}
