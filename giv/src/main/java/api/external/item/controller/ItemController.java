package api.external.item.controller;

import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.entity.ItemRequest;
import api.external.item.responsehistory.ItemResponseHistory;
import api.external.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Manages CRU (Create Read Update) operations of item.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@RestController
public class ItemController {
  @Autowired
  private ItemService itemService;

  /**
   * Retrieves all items in the form of Pages.
   *
   * @param pageNo   - pageNo to display. Default is 0, can be overridden by caller.
   * @param pageSize - pageSize to display. Default is 10, can be overridden by caller.
   * @param sortBy   - sortBy which key? Default is lastUpdatedDateTime, can be overridden by caller.
   * @param orderBy  - orderBy - Default is descending, can be overridden by caller.
   * @return Page<Item> - on success, returns a page of items (could be empty). Otherwise, a global rest
   * exception handler is automatically called and a context-sensitive error message is displayed.
   */
  @GetMapping("/items")
  public Page<Item> getAllItems(@RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "lastUpdatedDateTime") String sortBy,
                                @RequestParam(defaultValue = "D") String orderBy) {
    return itemService.getAllItems(pageNo, pageSize, sortBy, orderBy);
  }

  /**
   * This retrieves a single item based on a composite primary key metioned below.
   *
   * @param companyKey    - id of the company
   * @param divisionKey   - id of the division
   * @param warehouseKey  - id of the warehouse
   * @param skuBarcodeKey - represents the skuBarcode (usually a number)
   * @param season        - represents the season (winter or summer)
   * @param seasonYear    - represents the seasonYear (2019)
   * @param style         - represents the style of the item
   * @param styleSfx      - represents the style suffix of the item (if any)
   * @param color         - represents the color of the item
   * @param colorSfx      - represents the color suffix of the item (if any)
   * @param secDimension  - represents if the item is a second sale item
   * @param quality       - represents the quality of the item
   * @param sizeRngeCode  - represents the size range code
   * @param sizeRelPosnIn - represents the size relative position in table
   * @return Item         - This returns an item based on criteria.
   * @throws Exception - if the item does not exists
   */
  @GetMapping("/items/{companyKey},{divisionKey},{warehouseKey},{skuBarcodeKey}," +
      "{season},{seasonYear},{style},{styleSfx},{color},{colorSfx},{secDimension},{quality}," +
      "{sizeRngeCode},{sizeRelPosnIn}")

  public Item getItem(@PathVariable String companyKey, @PathVariable String divisionKey,
                      @PathVariable String warehouseKey, @PathVariable String skuBarcodeKey,
                      @PathVariable String season, @PathVariable String seasonYear,
                      @PathVariable String style, @PathVariable String styleSfx,
                      @PathVariable String color, @PathVariable String colorSfx,
                      @PathVariable String secDimension, @PathVariable String quality,
                      @PathVariable String sizeRngeCode, @PathVariable String sizeRelPosnIn) throws Exception {
    return itemService.getItem(new ItemKey(companyKey, divisionKey, warehouseKey, skuBarcodeKey, season, seasonYear,
        style, styleSfx, color,
        colorSfx, secDimension, quality, sizeRngeCode, sizeRelPosnIn));
  }

  /**
   * This adds an item.
   *
   * @param itemRequest - This is the json payload containing itemRequest.
   * @return ItemResponseHistory - Returns an appropriate response.
   */
  @PostMapping("/items")
  public ItemResponseHistory item(@RequestBody ItemRequest itemRequest) throws Exception {
    return itemService.addOrUpdate(itemRequest);
  }
}