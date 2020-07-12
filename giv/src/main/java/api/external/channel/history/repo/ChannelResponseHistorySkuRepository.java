package api.external.channel.history.repo;


import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.channel.history.entity.ChannelResponseHistorySku;

/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-17
 * 
 * This class represents repository for ChannelResponseHistorySku entity
 *
 */
@Repository
public interface ChannelResponseHistorySkuRepository extends PagingAndSortingRepository<ChannelResponseHistorySku, Long> {
	
	Page<ChannelResponseHistorySku> findByTransactionNumberIgnoreCaseContaining(String transactionNumber, Pageable pageable);
	
	Page<ChannelResponseHistorySku> findByChannelIgnoreCaseContaining(String channel, Pageable pageable);
	
	Page<ChannelResponseHistorySku> findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(Pageable pageable, String transactionNumber, String division, String warehouse, String channel, String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);
}
