package api.external.mfg.history.repo;


import api.external.mfg.history.entity.MfgResponseHistorySku;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the MfgResponseHistorySku repository for the mfg responseHistory sku.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-11-01
 */
@Repository
public interface MfgResponseHistorySkuRepository extends PagingAndSortingRepository<MfgResponseHistorySku, Long> {
	Page<MfgResponseHistorySku> findAll(Pageable pageable);
	
	Page<MfgResponseHistorySku> findByTransactionNumberIgnoreCaseContaining(String transactionNumber, Pageable pageable);
	
	Page<MfgResponseHistorySku> findByTransactionNumberIgnoreCaseContainingAndManufacturingPlantCodeIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(Pageable pageable, String transactionNumber, String manufacturingPlant, String skuBarcode, LocalDateTime userSuppliedStartDate, LocalDateTime userSuppliedEndDate);                                                
}