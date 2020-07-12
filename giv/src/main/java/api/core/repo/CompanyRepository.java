package api.core.repo;

import api.core.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This represents the repository for Company entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-11-23
 */
@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
  Page<Company> findAll(Pageable pageable);
}
