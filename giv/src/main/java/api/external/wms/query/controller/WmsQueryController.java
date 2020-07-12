package api.external.wms.query.controller;

import api.external.wms.query.entity.QueryInventoryDetailsRequest;
import api.external.wms.query.entity.QueryInventoryDetailsResponse;
import api.external.wms.query.entity.QueryInventoryTransactionsRequest;
import api.external.wms.query.entity.QueryInventoryTransactionsResponse;
import api.external.wms.query.service.WmsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines various API's for querying inventory.
 * @author : Sachin Kulkarni
 * @date : 05-11-2019
 */
@RestController
public class WmsQueryController {
    @Autowired
    private WmsQueryService wmsQueryService;

    /**
     * This API is used to query inventory transactions.
     *
     * @param queryInventoryTransactionsRequest   This is the request body.
     * @return QueryInventoryTransactionsResponse This returns the skus from inventory transaction record.
     */
    @PostMapping("/QueryInventoryTransactions")
    public QueryInventoryTransactionsResponse queryInventoryTransactions(@RequestBody QueryInventoryTransactionsRequest queryInventoryTransactionsRequest) throws Exception {
        return wmsQueryService.queryInventoryTransactions(queryInventoryTransactionsRequest);
    }

    /**
     * This API is used to query inventory transactions.
     *
     * @param queryInventoryDetailsRequest   This is the request body.
     * @return QueryInventoryDetailsResponse This returns the skus from sku inventory record.
     */
    @PostMapping("/QueryInventoryDetails")
    public QueryInventoryDetailsResponse QueryInventoryDetails(@RequestBody QueryInventoryDetailsRequest queryInventoryDetailsRequest) throws Exception {
        return wmsQueryService.queryInventoryDetails(queryInventoryDetailsRequest);
    }
}
