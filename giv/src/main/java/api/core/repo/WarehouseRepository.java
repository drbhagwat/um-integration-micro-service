package api.core.repo;

import api.core.entities.Warehouse;
import api.core.entities.WarehouseKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This represents repository for Warehouse entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 2.0
 * @since : 2019-04-15
 * @since : 2019-11-23
 */
@Repository
public interface WarehouseRepository extends PagingAndSortingRepository<Warehouse, WarehouseKey> {
  /**
   * Finds all warehouses.
   *
   * @return - all warehouses in the database.
   */
  Page<Warehouse> findAll(Pageable pageable);
}
