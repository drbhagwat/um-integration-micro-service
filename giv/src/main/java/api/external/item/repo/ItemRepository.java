package api.external.item.repo;

import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the item repository for the item entity.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, ItemKey> {
  Page<Item> findAll(Pageable pageable);

  @Query("select i from Item i where i.id.company = ?1 and i.id.division = ?2 and i.id.season = ?3 and i.id" +
      ".seasonYear = ?4 and i.id.style = ?5 and i.id.styleSfx = ?6 and i.id.color = ?7 and i.id.colorSfx = ?8 and i" +
      ".id.secDimension = ?9 and i.id.quality = ?10 and i.id.sizeRngeCode = ?11 and i.id.sizeRelPosnIn = ?12")
  Item findByCompanyAndDivisionAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn
      (String company, String division, String season, String seasonYear, String style, String styleSfx, String color,
       String colorSfx, String secDimension, String quality, String sizeRngeCode, String sizeRelPosnIn);

  @Query("select i from Item i where i.id.company = ?1 and i.id.division = ?2 and i.id.warehouse =?3 and i.id.season = ?4 and i.id" +
	      ".seasonYear = ?5 and i.id.style = ?6 and i.id.styleSfx = ?7 and i.id.color = ?8 and i.id.colorSfx = ?9 and i" +
	      ".id.secDimension = ?10 and i.id.quality = ?11 and i.id.sizeRngeCode = ?12 and i.id.sizeRelPosnIn = ?13")
	  Item findByCompanyAndDivisionAndWarehouseAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn
	      (String company, String division, String warehouse, String season, String seasonYear, String style, String styleSfx, String color,
	       String colorSfx, String secDimension, String quality, String sizeRngeCode, String sizeRelPosnIn);

  
  @Query("select i from Item i where i.id.company = ?1 and i.id.division = ?2 and i.id.skuBarcode = ?3 and i.id" +
      ".season = ?4 and i.id.seasonYear = ?5 and i.id.style = ?6 and i.id.styleSfx = ?7 and i.id.color = ?8 and i.id" +
      ".colorSfx = ?9 and i.id.secDimension = ?10 and i.id.quality = ?11 and i.id.sizeRngeCode = ?12 and i.id" +
      ".sizeRelPosnIn = ?13")
  Item findByCompanyAndDivisionAndSkuBarcodeAndSeasonAndSeasonYearAndStyleAndStyleSfxAndColorAndColorSfxAndSecDimensionAndQualityAndSizeRngeCodeAndSizeRelPosnIn
      (String company, String division, String skuBarcode, String season, String seasonYear, String style,
       String styleSfx, String color,
       String colorSfx, String secDimension, String quality, String sizeRngeCode, String sizeRelPosnIn);

  @Query(value = "SELECT * FROM item WHERE sku_barcode ILIKE %?1% AND style ILIKE %?2% AND style_sfx ILIKE %?3% AND " +
      "color ILIKE %?4% AND color_sfx ILIKE %?5% AND size_rnge_code ILIKE %?6%", nativeQuery = true)
  Page<Item> search(Pageable pageable, String skuBarcode, String style, String styleSuffix, String color, String colorSfx, String size);
}