package api.external.inventory.repo;

import api.external.inventory.entity.InvTransaction;
import api.external.inventory.entity.SKUInventoryKey;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class represents repository for InvTransaction
 *
 * @author Sachin Kulkarni
 * @version 1.0
 * @since 2019-10-23
 */

@Repository
public interface InvTransactionRepository extends PagingAndSortingRepository<InvTransaction, SKUInventoryKey> {
    Page<InvTransaction> findAll(Pageable pageable);

    List<InvTransaction> findAllBySkuInventoryKeyAndLastUpdatedDateTimeAfter(SKUInventoryKey skuInventoryKey, LocalDateTime dateTime);

    List<InvTransaction> findAllByLastUpdatedDateTimeAfter(LocalDateTime dateTime);

    List<InvTransaction> findBySkuInventoryKeyAndChannelAndLastUpdatedDateTimeAfter(SKUInventoryKey inv, String channel, LocalDateTime dateTime);

    List<InvTransaction> findBySkuInventoryKeyAndLastUpdatedDateTimeAfter(SKUInventoryKey inv, LocalDateTime dateTime);
}
