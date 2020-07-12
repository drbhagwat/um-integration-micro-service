package api.external.inventory.repo;

import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.entity.ManufacturingInventoryKey;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class represents repository for manufacturing inventory
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-06-17
 */

@Repository
public interface ManufacturingInventoryRepository extends PagingAndSortingRepository<ManufacturingInventory, Integer> {
	Page<ManufacturingInventory> findAll(Pageable pageable);

	// Used to find a sku by mfg inventory id
	Optional<ManufacturingInventory> findById(ManufacturingInventoryKey manufacturingInventoryKey);

	@Query(value = "SELECT * FROM manufacturing_inventory WHERE production_order_number iLIKE %?1% AND manufacturing_plant_code iLIKE %?2% AND style iLIKE %?3% AND style_sfx LIKE %?4% AND color LIKE %?5% AND quality LIKE %?6% AND size_rnge_code LIKE %?7% AND sku_barcode LIKE %?8% AND lot_number LIKE %?9% AND product_status LIKE %?10% AND sku_attribute1 LIKE %?11% AND country_of_origin LIKE %?12%", nativeQuery = true)
	Page<ManufacturingInventory> search(Pageable pageable, String productionOrderNumber, String manufacturingPlantCode,
			String style, String styleSfx, String color, String quality, String sizeRngeCode, String skuBarcode,
			String lotNumber, String productStatus, String skuAttribute1, String countryOfOrigin);
}
