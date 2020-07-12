package api.core.repo;

import api.core.entities.Division;
import api.core.entities.DivisionKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for Division entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-02-04
 */
@Repository
public interface DivisionRepository extends PagingAndSortingRepository<Division, DivisionKey> {
  Page<Division> findAll(Pageable pageable);
}
