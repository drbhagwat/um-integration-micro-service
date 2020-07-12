package api.external.inventory.repo;

import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This is an interface for Sku Inventory repository
 *
 * @author : Sachin Kulkarni
 * @version : 2.0
 * @since : 2019-05-02
 */

@Repository
public interface SkuInventoryRepository extends PagingAndSortingRepository<SkuInventory, SKUInventoryKey> {
  Page<SkuInventory> findAll(Pageable pageable);

  Optional<SkuInventory> findById(SKUInventoryKey skuInventoryKey);

  @Query(value = "select s from SkuInventory s where s.id.style LIKE %?1% And s.id.styleSfx LIKE %?2% And s.id.color " +
      "LIKE %?3% And " +
      "s.id.quality LIKE %?4% And s.id.sizeRngeCode LIKE %?5% And s.id.skuBarcode LIKE %?6% And s.id.lotNumber LIKE " +
      "%?7% And " +
      "s.id.productStatus LIKE %?8% And s.id.skuAttribute1 LIKE %?9% And s.id.countryOfOrigin LIKE %?10%")
  Page<SkuInventory> search(Pageable pageable, String style, String styleSfx, String color, String quality,
                            String sizeRngeCode, String skuBarcode, String lotNumber, String productStatus,
                            String skuAttribute1, String countryOfOrigin);

  List<SkuInventory> findAllByLastUpdatedDateTimeAfter(LocalDateTime dateTime);
}