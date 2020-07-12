package api;

import api.core.errors.CompanyCodeMandatory;
import api.core.repo.ChannelRepository;
import api.external.errors.ActionMandatory;
import api.external.inventory.entity.InvTransaction;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.InvTransactionRepository;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import api.external.wms.errors.InvalidQueryInventoryDetailsAction;
import api.external.wms.errors.InvalidQueryTransactionsAction;
import api.external.wms.errors.InvalidRequestFor;
import api.external.wms.errors.SkuEmpty;
import api.external.wms.query.entity.QueryInventoryDetailsRequest;
import api.external.wms.query.entity.QueryInventoryTransactionsRequest;
import api.external.wms.query.service.WmsQueryService;
import api.external.wms.validation.WmsValidationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @author : Sachin Kulkarni
 * @date : 31-01-2020
 */
@RunWith(MockitoJUnitRunner.class)
public class WmsQueryServiceTest {
    @InjectMocks
    private WmsQueryService wmsQueryService;

    @Mock
    private WmsValidationService wmsValidationService;

    @Mock
    private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

    @Mock
    private InvTransactionRepository invTransactionRepository;

    @Mock
    private SkuInventoryRepository skuInventoryRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Test
    public void testQueryInventoryTransactionsForInvalidAction() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        when(wmsValidationService.validateQueryTransactionsAction(request.getAction())).thenThrow(InvalidQueryTransactionsAction.class);
        assertThrows(Exception.class, () -> wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForInValidRequestFor() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        request.setAction("QTR");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR"});
        request.setRequestFor("S");
        when(wmsValidationService.validateWMSQueryRequestFor(request.getRequestFor())).thenThrow(InvalidRequestFor.class);
        assertThrows(Exception.class, () -> wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForEmptySku() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        request.setAction("QTR");
        request.setRequestFor("P");
        request.setSkus(new ArrayList<>());
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR"});
        when(wmsValidationService.validateSkuListSize(0)).thenThrow(SkuEmpty.class);
        assertThrows(Exception.class, () -> wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForInvalidSku() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        request.setAction("QTR");
        request.setRequestFor("P");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR"});
        when(wmsValidationService.validateSkuListSize(request.getSkus().size())).thenReturn(anyInt());
        for (SKUInventoryKey skuInventoryKey : request.getSkus()) {
            skuInventoryKey.setCompany(null);
            when(skuInventoryKeyValidationService.validate(skuInventoryKey)).thenThrow(CompanyCodeMandatory.class);
        }
        assertThrows(Exception.class, () -> wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForValidJSONDataForQTRAction() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        List<InvTransaction> invTransactions = new ArrayList<>();
        invTransactions.add(new InvTransaction(1L, new SKUInventoryKey("LEONISA", "LEONISA", "CSB",
                "887921535411", "", "", "A10000", "6", "403", "",
                "", "", "1170", "1", "F", "",
                "", "", "", "", "", "", ""),
                1.1, 1.1, "C100", "Retail", "ReasonCode",
                "User", request.getFromTimeStamp(), true));
        request.setAction("QTR");
        request.setRequestFor("P");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR", "QTA", "QTCH"});
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryRequestFor", new String[]{"A", "P"});
        for (SKUInventoryKey skuInventoryKey : request.getSkus()) {
            doReturn(invTransactions).when(invTransactionRepository).findAllBySkuInventoryKeyAndLastUpdatedDateTimeAfter(skuInventoryKey,
                    request.getFromTimeStamp());
        }
        assertNotNull(wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForValidJSONDataForQTAAction() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        request.setAction("QTA");
        request.setRequestFor("A");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR", "QTA"});
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryRequestFor", new String[]{"A", "P"});
        assertNotNull(wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryTransactionsForValidJSONDataForQTCHAction() throws Exception {
        QueryInventoryTransactionsRequest request = setQueryInventoryTransactions();
        request.setChannel("Retail");
        request.setAction("QTCH");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryTransactionsActions", new String[]{"QTR", "QTA", "QTCH"});
        for (SKUInventoryKey skuInventoryKey : request.getSkus()) {
            when(skuInventoryRepository.findById(skuInventoryKey)).thenReturn(Optional.of(createSkuInventory()));
        }
        assertNotNull(wmsQueryService.queryInventoryTransactions(request));
    }

    @Test
    public void testQueryInventoryDetailsForInvalidAction() throws InvalidQueryInventoryDetailsAction, ActionMandatory {
        QueryInventoryDetailsRequest request = setQueryInventoryDetails();
        when(wmsValidationService.validateQueryInventoryDetailsAction(any(String.class))).thenThrow(InvalidQueryInventoryDetailsAction.class);
        assertThrows(Exception.class, () -> wmsQueryService.queryInventoryDetails(request));
    }

    @Test
    public void testQueryInventoryDetailsForValidJSONDataForQIAction() throws Exception {
        QueryInventoryDetailsRequest request = setQueryInventoryDetails();
        request.setAction("QI");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryInventoryDetailsActions", new String[]{"QI"});
        assertNotNull(wmsQueryService.queryInventoryDetails(request));
    }

    @Test
    public void testQueryInventoryDetailsForValidJSONDataForQIAAction() throws Exception {
        QueryInventoryDetailsRequest request = setQueryInventoryDetails();
        request.setAction("QIA");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryInventoryDetailsActions", new String[]{"QI", "QIA"});
        assertNotNull(wmsQueryService.queryInventoryDetails(request));
    }

    @Test
    public void testQueryInventoryDetailsForValidJSONDataForQICAction() throws Exception {
        QueryInventoryDetailsRequest request = setQueryInventoryDetails();
        request.setAction("QIC");
        ReflectionTestUtils.setField(wmsQueryService, "wmsQueryInventoryDetailsActions", new String[]{"QI", "QIA", "QIC"});
        assertNotNull(wmsQueryService.queryInventoryDetails(request));
    }

    private QueryInventoryTransactionsRequest setQueryInventoryTransactions() {
        return new QueryInventoryTransactionsRequest(LocalDateTime.now(), "QTR", "P", "G", null,
                Arrays.asList(new SKUInventoryKey("LEONISA", "LEONISA", "CSB", "887921535411",
                                "", "", "A10000", "6", "403", "", "", "",
                                "1170", "1", "F", "", "", "",
                                "", "", "", "", ""),
                        new SKUInventoryKey("LEONISA", "LEONISA", "CSB", "887921535411",
                                "", "", "A10000", "6", "403", "", "", "",
                                "1170", "1", "F", "", "", "",
                                "", "", "", "", "")));
    }

    private QueryInventoryDetailsRequest setQueryInventoryDetails() {
        return new QueryInventoryDetailsRequest(LocalDateTime.now(), "Retail", "QI", "G",
                Collections.singletonList(new SKUInventoryKey("LEONISA", "LEONISA", "CSB", "887921535411",
                        "", "", "A10000", "6", "403", "", "", "",
                        "1170", "1", "F", "", "", "",
                        "", "", "", "", "")));
    }

    private SkuInventory createSkuInventory() {
        return new SkuInventory(new SKUInventoryKey("LEONISA", "LEONISA", "CSB", "887921535411",
                "", "", "A10000", "6", "403", "", "", "",
                "1170", "1", "F", "", "", "",
                "", "", "", "", ""), "1910",
                100.0, 100.0, 100.0, 50.0, 100.0);
    }
}
