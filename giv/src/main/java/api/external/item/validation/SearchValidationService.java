package api.external.item.validation;

import api.external.item.entity.ItemLogSearch;
import api.external.item.requesthistory.SearchCriteria;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * This class describes the item validation service.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-25
 */
@Component
public class SearchValidationService {
  private ItemLogSearch itemLogSearch;

  public ItemLogSearch getRefinedSearch(SearchCriteria searchCriteria) {
    itemLogSearch = new ItemLogSearch();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String userSuppliedStartDate = searchCriteria.getUserSuppliedStartDate();
    LocalDate ldtUserSuppliedStartDate;

    if ((userSuppliedStartDate == null) || userSuppliedStartDate.equals("*") || userSuppliedStartDate.equals("")) {
      ldtUserSuppliedStartDate = LocalDate.of(2010, 12, 31).atStartOfDay().toLocalDate();
    } else {
      ldtUserSuppliedStartDate = LocalDate.parse(userSuppliedStartDate, dateTimeFormatter);
    }
    itemLogSearch.setLdtUserSuppliedStartDate(ldtUserSuppliedStartDate);

    String userSuppliedEndDate = searchCriteria.getUserSuppliedEndDate();
    LocalDate ldtUserSuppliedEndDate;

    if ((userSuppliedEndDate == null) || userSuppliedEndDate.equals("*") || userSuppliedEndDate.equals("")) {
      ldtUserSuppliedEndDate = LocalDate.of(2118, 12, 31).atStartOfDay().toLocalDate();
    } else {
      ldtUserSuppliedEndDate = LocalDate.parse(userSuppliedEndDate, dateTimeFormatter);
    }
    itemLogSearch.setLdtUserSuppliedEndDate(ldtUserSuppliedEndDate);

    byte divisionCompare;
    byte warehouseCompare;
    byte skuBarcodeCompare;
    String division = searchCriteria.getDivision();
    itemLogSearch.setDivision(division);

    if ((division == null) || (division.equals("*")) || (division.trim().equals(""))) {
      divisionCompare = 0;
    }
    else {
      divisionCompare = 1;
    }
    String warehouse = searchCriteria.getWarehouse();
    itemLogSearch.setWarehouse(warehouse);

    if ((warehouse == null) || (warehouse.equals("*")) || (warehouse.trim().equals(""))) {
      warehouseCompare = 0;
    } else {
      warehouseCompare = 2;
    }
    String skuBarcode = searchCriteria.getSkuBarcode();
    itemLogSearch.setSkuBarcode(skuBarcode);

    if ((skuBarcode == null) || (skuBarcode.equals("*")) || (skuBarcode.trim().equals(""))) {
      skuBarcodeCompare = 0;
    } else {
      skuBarcodeCompare = 4;
    }
    itemLogSearch.setFinalCompare((byte) (divisionCompare | warehouseCompare | skuBarcodeCompare));
    return itemLogSearch;
  }
}