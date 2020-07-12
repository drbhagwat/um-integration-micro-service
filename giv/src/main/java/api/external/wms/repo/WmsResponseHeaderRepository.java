package api.external.wms.repo;


import api.external.wms.entity.WmsResponseHeader;
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
public interface WmsResponseHeaderRepository extends PagingAndSortingRepository<WmsResponseHeader, Long> {
    WmsResponseHeader findByTransactionNumber(String transactionNumber);
}
