package api.external.channel.history.repo;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import api.external.channel.history.entity.ChannelRequestHistorySku;

/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-17
 * 
 * This class represents repository for ChannelRequestHistorySku entity
 *
 */
@Repository
public interface ChannelRequestHistorySkuRepository extends PagingAndSortingRepository<ChannelRequestHistorySku, Long> {
	Page<ChannelRequestHistorySku> findAll(Pageable pageable);
	
	Page<ChannelRequestHistorySku> findByTransactionNumberIgnoreCaseContaining(String transactionNumber, Pageable pageable);
	
	Page<ChannelRequestHistorySku> findByChannelIgnoreCaseContaining(String channel, Pageable pageable);
	
	Page<ChannelRequestHistorySku> findByTransactionNumberIgnoreCaseContainingAndDivisionIgnoreCaseContainingAndWarehouseIgnoreCaseContainingAndChannelIgnoreCaseContainingAndSkuBarcodeIgnoreCaseContainingAndDateTimeStampAfterAndDateTimeStampBefore(Pageable pageable, String transactionNumber, String division, String warehouse, String channel, String skuBarcode, LocalDateTime userSuppliedStartDateStamp, LocalDateTime userSuppliedEndDateStamp);
}
