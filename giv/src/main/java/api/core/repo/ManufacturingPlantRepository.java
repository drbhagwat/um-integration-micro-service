package api.core.repo;

import api.core.entities.ManufacturingPlant;
import api.core.entities.ManufacturingPlantKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *  This class represents repository for ManufacturingPlant entity.
 *
 *  @author : Thamilarasi
 *  @version : 1.0
 *  @since : 2019-06-13
 */
@Repository
public interface ManufacturingPlantRepository extends PagingAndSortingRepository<ManufacturingPlant, ManufacturingPlantKey>{
	Page<ManufacturingPlant> findAll(Pageable pageable);
}
