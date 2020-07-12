package api.external.wms.query.service;

import api.core.entities.Channel;
import api.core.repo.ChannelRepository;
import api.external.campaign.errors.DateTimeStampFieldIsRequired;
import api.external.inventory.entity.InvTransaction;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.InvTransactionRepository;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import api.external.wms.query.entity.QueryInventoryDetailsRequest;
import api.external.wms.query.entity.QueryInventoryDetailsResponse;
import api.external.wms.query.entity.QueryInventoryDetailsSkuResponse;
import api.external.wms.query.entity.QueryInventoryTransactionsRequest;
import api.external.wms.query.entity.QueryInventoryTransactionsResponse;
import api.external.wms.query.entity.QueryInventoryTransactionsSkuResponse;
import api.external.wms.validation.WmsValidationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This is the service or the implementation class which provides various services for querying inventory.
 *
 * @author : Sachin Kulkarni
 * @date : 06-11-2019
 */
@Service
public class WmsQueryService {
    @Autowired
    private WmsValidationService wmsValidationService;

    @Autowired
    private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

    @Autowired
    private InvTransactionRepository invTransactionRepository;

    @Autowired
    private SkuInventoryRepository skuInventoryRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Value("${DATE_FIELD_CANNOT_BE_NULL_OR_BLANK}")
    private String dateFieldCannotBeNullOrBlank;

    @Value("${WMS_QUERY_REQUEST_FOR}")
    private String[] wmsQueryRequestFor;

    @Value("${WMS_QUERY_TRANSACTIONS_ACTIONS}")
    private String[] wmsQueryTransactionsActions;

    @Value("${WMS_QUERY_INVENTORY_DETAILS_ACTIONS}")
    private String[] wmsQueryInventoryDetailsActions;

    // This method returns QueryInventoryTransactionsResponse by querying inventory transactions
    public QueryInventoryTransactionsResponse queryInventoryTransactions(QueryInventoryTransactionsRequest request) throws Exception {
        LocalDateTime dateTime = Optional.ofNullable(request.getFromTimeStamp()).orElseThrow(() -> new DateTimeStampFieldIsRequired(dateFieldCannotBeNullOrBlank));
        String action = request.getAction();
        String requestFor = request.getRequestFor();
        String channel = request.getChannel();
        List<SKUInventoryKey> skuInventoryKeyList = request.getSkus();
        List<QueryInventoryTransactionsSkuResponse> queryInvTransSkuResponses = new ArrayList<>();

        // Validating action field
        wmsValidationService.validateQueryTransactionsAction(action);

        // Validating inventory source
        wmsValidationService.validateWmsInventorySource(request.getInventorySource());

        // Query transactions by request (Allocated or Protected)
        if (action.equals(wmsQueryTransactionsActions[0])) {
            wmsValidationService.validateWMSQueryRequestFor(requestFor);
            wmsValidationService.validateSkuListSize(skuInventoryKeyList.size());

            for (SKUInventoryKey skuInventoryKey : skuInventoryKeyList) {
                SKUInventoryKey key = stripTrailing(skuInventoryKey);

                // Validating json key fields
                skuInventoryKeyValidationService.validate(key);

                List<InvTransaction> invTransList = invTransactionRepository.findAllBySkuInventoryKeyAndLastUpdatedDateTimeAfter(key, dateTime);
                queryInvTransSkuResponses.addAll(setResponseByRequest(invTransList, requestFor));
            }
        } else if (action.equals(wmsQueryTransactionsActions[1])) { // Query transactions all
            wmsValidationService.validateWMSQueryRequestFor(requestFor);
            List<InvTransaction> invTransList = invTransactionRepository.findAllByLastUpdatedDateTimeAfter(dateTime);
            queryInvTransSkuResponses.addAll(setResponseByRequest(invTransList, requestFor));
        } else if (action.equals(wmsQueryTransactionsActions[2])) { // Query transactions by channel
            wmsValidationService.validateChannel(channel);
            wmsValidationService.validateSkuListSize(skuInventoryKeyList.size());

            for (SKUInventoryKey skuInventoryKey : skuInventoryKeyList) {
                SKUInventoryKey key = stripTrailing(skuInventoryKey);

                // Validating json key fields
                skuInventoryKeyValidationService.validate(key);

                Optional<SkuInventory> skuInventory = skuInventoryRepository.findById(key);
                skuInventory.ifPresent(inventory -> Optional.of(channel).filter(ch -> channel.equals("*")).ifPresentOrElse(s -> {
                    List<Channel> channels = (List<Channel>) channelRepository.findAll();
                    channels.forEach(ch -> // adding records of all channels
                            queryInvTransSkuResponses.add(calcAllocAndProtectQty(inventory.getId(), ch.getId(), dateTime,
                                    inventory.getOnHandQuantity(), inventory.getAvailableQuantity(), inventory.getLockedQuantity())));
                }, () -> // adding records of specific channel
                        queryInvTransSkuResponses.add(calcAllocAndProtectQty(inventory.getId(), channel, dateTime,
                                inventory.getOnHandQuantity(), inventory.getAvailableQuantity(), inventory.getLockedQuantity()))));
            }
        }
        return new QueryInventoryTransactionsResponse(dateTime, action, channel, requestFor, request.getInventorySource(),
                queryInvTransSkuResponses.stream().filter(response -> response.getChannel() != null).collect(Collectors.toList()));
    }

    // This method returns QueryInventoryDetailsResponse by querying the sku inventory.
    public QueryInventoryDetailsResponse queryInventoryDetails(QueryInventoryDetailsRequest request) throws Exception {
        LocalDateTime localDateTime = Optional.ofNullable(request.getFromTimeStamp()).
                orElseThrow(() -> new DateTimeStampFieldIsRequired(dateFieldCannotBeNullOrBlank));
        String action = request.getAction();
        List<SkuInventory> skuInventoryList = new ArrayList<>();

        // Validating action field
        wmsValidationService.validateQueryInventoryDetailsAction(action);

        // Validating inventory source
        wmsValidationService.validateWmsInventorySource(request.getInventorySource());

        if (action.equals(wmsQueryInventoryDetailsActions[0])) { // Query inventory
            List<SKUInventoryKey> skuInventoryKeyList = request.getSkus();
            wmsValidationService.validateSkuListSize(skuInventoryKeyList.size());

            for (SKUInventoryKey skuInventoryKey : skuInventoryKeyList) {
                SKUInventoryKey key = stripTrailing(skuInventoryKey);

                // Validating json key fields
                skuInventoryKeyValidationService.validate(key);

                Optional<SkuInventory> skuInventory = skuInventoryRepository.findById(key);
                skuInventory.ifPresent(skuInventoryList::add);
            }
        } else if (action.equals(wmsQueryInventoryDetailsActions[1])) { // Query inventory all
            skuInventoryList.addAll((List<SkuInventory>) skuInventoryRepository.findAll());
        } else if (action.equals(wmsQueryInventoryDetailsActions[2])) { // Query inventory change
            skuInventoryList.addAll(skuInventoryRepository.findAllByLastUpdatedDateTimeAfter(localDateTime));
        }

        return new QueryInventoryDetailsResponse(request.getFromTimeStamp(), action, request.getInventorySource(),
                request.getChannel(), setQueryInvDetailsSkuResponse(skuInventoryList));
    }

    // This method returns QueryInventoryTransactionsSkuResponse by setting QueryInventoryTransactionsSkuResponse
    private QueryInventoryTransactionsSkuResponse setQueryInvTransSkuResponse(InvTransaction invTransaction) {
        SKUInventoryKey key = invTransaction.getSkuInventoryKey();

        // Setting onHand, available, locked and serial field to null as these fields are ignored in the json output.
        return new QueryInventoryTransactionsSkuResponse(key.getCompany(), key.getDivision(), key.getWarehouse(), key.getSkuBarcode(),
                key.getSeason(), key.getSeasonYear(), key.getStyle(), key.getStyleSfx(), key.getColor(), key.getColorSfx(),
                key.getSecDimension(), key.getQuality(), key.getSizeRngeCode(), key.getSizeRelPosnIn(), key.getInventoryType(),
                key.getLotNumber(), key.getCountryOfOrigin(), key.getProductStatus(), key.getSkuAttribute1(), key.getSkuAttribute2(),
                key.getSkuAttribute3(), key.getSkuAttribute4(), key.getSkuAttribute5(), invTransaction.getAllocatedQuantity(),
                invTransaction.getProtectedQuantity(), invTransaction.getCampaignCode(), invTransaction.getChannel(),
                invTransaction.getReasonCode(), null, null, null, null);
    }

    // This method returns List of QueryInventoryDetailsSkuResponse by adding response sku to queryInvDetailsSkuResponseList
    private List<QueryInventoryDetailsSkuResponse> setQueryInvDetailsSkuResponse(List<SkuInventory> skuInventoryList) {
        List<QueryInventoryDetailsSkuResponse> queryInvDetailsSkuResponseList = new ArrayList<>();
        skuInventoryList.forEach(skuInventory -> {
            SKUInventoryKey key = skuInventory.getId();

            // Setting channel field to null as this field is ignored in the json output.
            queryInvDetailsSkuResponseList.add(new QueryInventoryDetailsSkuResponse(key.getCompany(), key.getDivision(),
                    key.getWarehouse(), null, key.getSkuBarcode(), key.getSeason(), key.getSeasonYear(), key.getStyle(),
                    key.getStyleSfx(), key.getColor(), key.getColorSfx(), key.getSecDimension(), key.getQuality(), key.getSizeRngeCode(),
                    key.getSizeRelPosnIn(), key.getInventoryType(), key.getLotNumber(), key.getCountryOfOrigin(), key.getProductStatus(),
                    key.getSkuAttribute1(), key.getSkuAttribute2(), key.getSkuAttribute3(), key.getSkuAttribute4(), key.getSkuAttribute5(),
                    skuInventory.getAllocatedQuantity(), skuInventory.getProtectedQuantity(), skuInventory.getOnHandQuantity(),
                    skuInventory.getAvailableQuantity(), skuInventory.getLockedQuantity(), skuInventory.getSerialNumber()));
        });
        return queryInvDetailsSkuResponseList;
    }

    // This method returns List of QueryInventoryTransactionsSkuResponse by filtering InvTransaction List based on request
    private List<QueryInventoryTransactionsSkuResponse> setResponseByRequest(List<InvTransaction> transList, String requestFor) {
        List<QueryInventoryTransactionsSkuResponse> queryInvTransSkuResponses = new ArrayList<>();
        transList.forEach(invTransaction -> {
            QueryInventoryTransactionsSkuResponse queryInvTransSkuResponse = setQueryInvTransSkuResponse(invTransaction);

            if (requestFor.equals(wmsQueryRequestFor[0]) && invTransaction.getAllocatedQuantity() != 0) { // Request for allocate
                queryInvTransSkuResponse.setProtectedQuantity(null); // protect qty field will be ignored in the json output
                queryInvTransSkuResponses.add(queryInvTransSkuResponse);
            } else if (requestFor.equals(wmsQueryRequestFor[1]) && invTransaction.getProtectedQuantity() != 0) { // Request for protect
                queryInvTransSkuResponse.setAllocatedQuantity(null); // allocate qty field will be ignored in the json output
                queryInvTransSkuResponses.add(queryInvTransSkuResponse);
            }
        });

        return queryInvTransSkuResponses;
    }

    // This method returns SKUInventoryKey by removing trailing whitespaces of company, division and warehouse fields.
    private SKUInventoryKey stripTrailing(SKUInventoryKey skuInventoryKey) {
        Optional.ofNullable(skuInventoryKey.getCompany()).ifPresent(company -> skuInventoryKey.setCompany(company.stripTrailing()));
        Optional.ofNullable(skuInventoryKey.getDivision()).ifPresent(division -> skuInventoryKey.setDivision(division.stripTrailing()));
        Optional.ofNullable(skuInventoryKey.getWarehouse()).ifPresent(warehouse -> skuInventoryKey.setWarehouse(warehouse.stripTrailing()));

        return skuInventoryKey;
    }

    // This method returns QueryInventoryTransactionsSkuResponse by calculating Allocated and Protected Qty
    private QueryInventoryTransactionsSkuResponse calcAllocAndProtectQty(SKUInventoryKey id, String channel, LocalDateTime localDateTime,
                                                                         double onHandQty, double availableQty, double lockedQty) {
        List<InvTransaction> invTransactionList =
                invTransactionRepository.findBySkuInventoryKeyAndChannelAndLastUpdatedDateTimeAfter(id, channel, localDateTime);
        QueryInventoryTransactionsSkuResponse queryInvTransSkuResponse = new QueryInventoryTransactionsSkuResponse();
        double allocatedQty = 0;
        double protectedQty = 0;

        for (InvTransaction invTransaction : invTransactionList) {
            allocatedQty += invTransaction.getAllocatedQuantity();
            protectedQty += invTransaction.getProtectedQuantity();
            queryInvTransSkuResponse = setQueryInvTransSkuResponse(invTransaction);
        }

        queryInvTransSkuResponse.setOnHandQty(onHandQty);
        queryInvTransSkuResponse.setAvailableQty(availableQty);
        queryInvTransSkuResponse.setLockedQty(lockedQty);
        queryInvTransSkuResponse.setAllocatedQuantity(allocatedQty);
        queryInvTransSkuResponse.setProtectedQuantity(protectedQty);

        return queryInvTransSkuResponse;
    }
}