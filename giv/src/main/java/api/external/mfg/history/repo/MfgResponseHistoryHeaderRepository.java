package api.external.mfg.history.repo;


import api.external.mfg.history.entity.MfgResponseHistoryHeader;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the MfgResponseHistoryHeader repository for the Mfg RequestHistory Header.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-11-01
 */
@Repository
public interface MfgResponseHistoryHeaderRepository extends PagingAndSortingRepository<MfgResponseHistoryHeader, Long> {
	MfgResponseHistoryHeader findByTransactionNumber(String transactionNumber);

}
