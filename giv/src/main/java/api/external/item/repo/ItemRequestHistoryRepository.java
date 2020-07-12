package api.external.item.repo;


import api.external.item.requesthistory.ItemRequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class is the item repository for the item request history.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Repository
public interface ItemRequestHistoryRepository extends PagingAndSortingRepository<ItemRequestHistory, Long> {
  @Query(value = "SELECT * FROM item_request_history", nativeQuery = true)
  Page<ItemRequestHistory> findAll(Pageable pageable);

  List<ItemRequestHistory> findAll();

  ItemRequestHistory findByTransactionNumber(String transactionNumber);

  @Query(value = "SELECT * FROM item_request_history WHERE transaction_number ILIKE %?1%", nativeQuery = true)
  List<ItemRequestHistory> findListByTransactionNumber(String transactionNumber);
}
