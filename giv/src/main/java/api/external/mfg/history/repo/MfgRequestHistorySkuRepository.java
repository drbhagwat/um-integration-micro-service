package api.external.mfg.history.repo;


import api.external.mfg.history.entity.MfgRequestHistorySku;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the MfgRequestHistorySku repository for the MfgRequestHistorySku.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-11-01
 */
@Repository
public interface MfgRequestHistorySkuRepository extends PagingAndSortingRepository<MfgRequestHistorySku, Long> {
Page<MfgRequestHistorySku> findAll(Pageable pageable);
	
	Page<MfgRequestHistorySku> findByTransactionNumberIgnoreCaseContaining(String transactionNumber, Pageable pageable);
	
	Page<MfgRequestHistorySku> findByTransactionNumberIgnoreCaseContainingAndManufacturingPlantCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(Pageable pageable, String transactionNumber, String manufacturingPlant, String skuBarcode, LocalDateTime userSuppliedStartDate, LocalDateTime userSuppliedEndDate);                                                
}
