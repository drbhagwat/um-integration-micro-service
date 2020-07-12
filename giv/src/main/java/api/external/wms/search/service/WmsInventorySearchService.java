package api.external.wms.search.service;

import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.wms.search.entity.WmsInventorySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Provides search service for skus in the sku inventory
 *
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-07-15
 */
@Service
public class WmsInventorySearchService {
    @Autowired
    private SkuInventoryRepository skuInventoryRepository;

    /**
     * @param wmsInvSearchCriteria - search parameters of the sku inventory in the db.
     * @param pageNo               - default is 0, can be overridden by caller.
     * @param pageSize             - default is 10, can be overridden by caller.
     * @param orderBy              - default is descending order, can be overridden by caller.
     * @return - on success, returns a page of skus in the sku inventory.
     * <p>
     * This method is used to find or search the skus in the sku inventory
     */
    public Page<SkuInventory> searchWmsInventory(WmsInventorySearchCriteria wmsInvSearchCriteria, Integer pageNo,
                                                 Integer pageSize, String orderBy) {
        String style = wmsInvSearchCriteria.getStyle();
        String styleSfx = wmsInvSearchCriteria.getStyleSfx();
        String color = wmsInvSearchCriteria.getColor();
        String quality = wmsInvSearchCriteria.getQuality();
        String sizeRngeCode = wmsInvSearchCriteria.getSizeRngeCode();
        String skuBarcode = wmsInvSearchCriteria.getSkuBarcode();
        String lotNumber = wmsInvSearchCriteria.getLotNumber();
        String productStatus = wmsInvSearchCriteria.getProductStatus();
        String skuAttribute1 = wmsInvSearchCriteria.getSkuAttribute1();
        String countryOfOrigin = wmsInvSearchCriteria.getCountryOfOrigin();

        // handle both wild card and when the search parameter is not present in search
        // criteria
        if ((style == null) || style.equals("*")) {
            style = "";
        } else {
            style = style.trim();
        }

        if ((styleSfx == null) || styleSfx.equals("*")) {
            styleSfx = "";
        } else {
            styleSfx = styleSfx.trim();
        }

        if ((color == null) || color.equals("*")) {
            color = "";
        } else {
            color = color.trim();
        }

        if ((quality == null) || quality.equals("*")) {
            quality = "";
        } else {
            quality = quality.trim();
        }

        if ((sizeRngeCode == null) || sizeRngeCode.equals("*")) {
            sizeRngeCode = "";
        } else {
            sizeRngeCode = sizeRngeCode.trim();
        }

        if ((skuBarcode == null) || skuBarcode.equals("*")) {
            skuBarcode = "";
        } else {
            skuBarcode = skuBarcode.trim();
        }

        if ((lotNumber == null) || lotNumber.equals("*")) {
            lotNumber = "";
        } else {
            lotNumber = lotNumber.trim();
        }

        if ((productStatus == null) || productStatus.equals("*")) {
            productStatus = "";
        } else {
            productStatus = productStatus.trim();
        }

        if ((skuAttribute1 == null) || skuAttribute1.equals("*")) {
            skuAttribute1 = "";
        } else {
            skuAttribute1 = skuAttribute1.trim();
        }

        if ((countryOfOrigin == null) || countryOfOrigin.equals("*")) {
            countryOfOrigin = "";
        } else {
            countryOfOrigin = countryOfOrigin.trim();
        }
        String sortBy = "lastUpdatedDateTime";
        Pageable paging = orderBy.equals("A") ? PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending())
                : PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return skuInventoryRepository.search(paging, style, styleSfx, color, quality, sizeRngeCode, skuBarcode, lotNumber
                , productStatus, skuAttribute1, countryOfOrigin);
    }
}