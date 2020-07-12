package api.external.wms.repo;

import api.external.wms.entity.WmsRequestHeader;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : Sachin Kulkarni
 * @date : 28-10-2019
 */
@Repository
public interface WmsRequestHeaderRepository extends PagingAndSortingRepository<WmsRequestHeader, Long> {
}
