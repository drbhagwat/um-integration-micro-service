package api.external.item.repo;


import api.external.item.responsehistory.ItemResponseHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class is the item repository for the item response history.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Repository
public interface ItemResponseHistoryRepository extends PagingAndSortingRepository<ItemResponseHistory, Long> {
  @Query(value = "SELECT * FROM item_response_history", nativeQuery = true)
  Page<ItemResponseHistory> findAll(Pageable pageable);
  
  List<ItemResponseHistory> findAll();

  ItemResponseHistory findByTransactionNumber(String transactionNumber);

  @Query(value = "SELECT * FROM item_response_history WHERE transaction_number ILIKE %?1%", nativeQuery = true)
  List<ItemResponseHistory> findListByTransactionNumber(String transactionNumber);
}
