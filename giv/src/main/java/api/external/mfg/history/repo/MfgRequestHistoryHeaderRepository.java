package api.external.mfg.history.repo;


import api.external.mfg.history.entity.MfgRequestHistoryHeader;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the MfgRequestHistoryHeader repository for the Mfg RequestHistory Header.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-11-01
 */
@Repository
public interface MfgRequestHistoryHeaderRepository extends PagingAndSortingRepository<MfgRequestHistoryHeader, Long> {

}
