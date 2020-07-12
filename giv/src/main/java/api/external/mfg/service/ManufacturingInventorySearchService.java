package api.external.mfg.service;

import api.external.inventory.entity.ManufacturingInventory;
import api.external.inventory.repo.ManufacturingInventoryRepository;
import api.external.mfg.entity.MfgInventorySearchCriteria;
import api.external.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Provides search service of skus in the manufacturing inventory
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-15
 */
@Service
public class ManufacturingInventorySearchService {
    @Autowired
    private ManufacturingInventoryRepository manufacturingInventoryRepository;

    /**
     * @param mfgInvSearchCriteria - search parameters of the manufacturing inventory in the db.
     * @param pageNo               - default is 0, can be overridden by caller.
     * @param pageSize             - default is 10, can be overridden by caller.
     * @param orderBy              - default is descending order, can be overridden by caller.
     * @return - on success, returns a page of skus in the manufacturing inventory.
     * <p>
     * This method is used to find or search the skus in the manufacturing inventory
     */
    public Page<ManufacturingInventory> searchMfgInventory(MfgInventorySearchCriteria mfgInvSearchCriteria,
                                                           Integer pageNo, Integer pageSize, String orderBy) {

        String productionOrderNumber = mfgInvSearchCriteria.getProductionOrderNumber();
        String manufacturingPlantCode = mfgInvSearchCriteria.getManufacturingPlantCode();

        String style = mfgInvSearchCriteria.getStyle();
        String styleSfx = mfgInvSearchCriteria.getStyleSfx();
        String color = mfgInvSearchCriteria.getColor();
        String quality = mfgInvSearchCriteria.getQuality();
        String sizeRngeCode = mfgInvSearchCriteria.getSizeRngeCode();
        String skuBarcode = mfgInvSearchCriteria.getSkuBarcode();
        String lotNumber = mfgInvSearchCriteria.getLotNumber();
        String productStatus = mfgInvSearchCriteria.getProductStatus();
        String skuAttribute1 = mfgInvSearchCriteria.getSkuAttribute1();
        String countryOfOrigin = mfgInvSearchCriteria.getCountryOfOrigin();
        String sortBy = mfgInvSearchCriteria.getSortBy();

        if (sortBy == null) {
            sortBy = "last_updated_date_time";
        } else {
            sortBy = Converter.toDatabaseColumnName(sortBy);
        }

        // handle both wild card and when the search parameter is not present in search
        // criteria

        if ((manufacturingPlantCode == null) || manufacturingPlantCode.equals("*") || manufacturingPlantCode.equals("")) {
            manufacturingPlantCode = "";
        } else {
            manufacturingPlantCode = manufacturingPlantCode.trim();
        }

        if ((productionOrderNumber == null) || productionOrderNumber.equals("*") || productionOrderNumber.equals("")) {
            productionOrderNumber = "";
        } else {
            productionOrderNumber = productionOrderNumber.trim();
        }

        if ((style == null) || style.equals("*") || style.equals("")) {
            style = "";
        } else {
            style = style.trim();
        }

        if ((styleSfx == null) || styleSfx.equals("*") || styleSfx.equals("")) {
            styleSfx = "";
        } else {
            styleSfx = styleSfx.trim();
        }

        if ((color == null) || color.equals("*") || color.equals("")) {
            color = "";
        } else {
            color = color.trim();
        }

        if ((quality == null) || quality.equals("*") || quality.equals("")) {
            quality = "";
        } else {
            quality = quality.trim();
        }

        if ((sizeRngeCode == null) || sizeRngeCode.equals("*") || sizeRngeCode.equals("")) {
            sizeRngeCode = "";
        } else {
            sizeRngeCode = sizeRngeCode.trim();
        }

        if ((skuBarcode == null) || skuBarcode.equals("*") || skuBarcode.equals("")) {
            skuBarcode = "";
        } else {
            skuBarcode = skuBarcode.trim();
        }

        if ((lotNumber == null) || lotNumber.equals("*") || lotNumber.equals("")) {
            lotNumber = "";
        } else {
            lotNumber = lotNumber.trim();
        }

        if ((productStatus == null) || productStatus.equals("*") || productStatus.equals("")) {
            productStatus = "";
        } else {
            productStatus = productStatus.trim();
        }

        if ((skuAttribute1 == null) || skuAttribute1.equals("*") || skuAttribute1.equals("")) {
            skuAttribute1 = "";
        } else {
            skuAttribute1 = skuAttribute1.trim();
        }

        if ((countryOfOrigin == null) || countryOfOrigin.equals("*") || countryOfOrigin.equals("")) {
            countryOfOrigin = "";
        } else {
            countryOfOrigin = countryOfOrigin.trim();
        }

        Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
                : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return manufacturingInventoryRepository.search(paging, productionOrderNumber, manufacturingPlantCode, style, styleSfx, color, quality, sizeRngeCode, skuBarcode, lotNumber, productStatus, skuAttribute1, countryOfOrigin);
    }
}