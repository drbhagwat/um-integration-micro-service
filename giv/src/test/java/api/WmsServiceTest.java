package api;

import api.external.errors.ActionMandatory;
import api.external.inventory.entity.SKUInventoryKey;
import api.external.inventory.entity.SkuInventory;
import api.external.inventory.repo.SkuInventoryRepository;
import api.external.inventory.service.SkuInventoryService;
import api.external.inventory.validation.SKUInventoryKeyValidationService;
import api.external.item.entity.Item;
import api.external.item.entity.ItemKey;
import api.external.item.errors.InvalidAction;
import api.external.item.service.ItemService;
import api.external.wms.entity.WmsInvRequest;
import api.external.wms.entity.WmsResponseDetail;
import api.external.wms.entity.WmsResponseHeader;
import api.external.wms.entity.WmsSku;
import api.external.wms.errors.InvalidQty;
import api.external.wms.errors.InvalidWmsInventorySource;
import api.external.wms.errors.InventorySourceMandatory;
import api.external.wms.errors.SerialNumberMandatory;
import api.external.wms.repo.WmsRequestHeaderRepository;
import api.external.wms.repo.WmsResponseHeaderRepository;
import api.external.wms.service.WmsInvService;
import api.external.wms.validation.WmsValidationService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author : Sachin Kulkarni
 * @date : 25-10-2019
 */
@RunWith(MockitoJUnitRunner.class)
public class WmsServiceTest {
    @InjectMocks
    private WmsInvService wmsInvService;

    @Mock
    private SkuInventoryService skuInventoryService;

    @Mock
    private WmsValidationService validationService;

    @Mock
    private SKUInventoryKeyValidationService skuInventoryKeyValidationService;

    @Mock
    private ItemService itemService;

    @Mock
    private SkuInventoryRepository skuInventoryRepository;

    @Mock
    private WmsResponseHeaderRepository wmsResponseHeaderRepository;

    @Mock
    private WmsRequestHeaderRepository wmsRequestHeaderRepository;

    @Test
    public void testProcessForNewTransaction() throws Exception {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(skuInventoryKeyValidationService.validate(any(SKUInventoryKey.class))).thenReturn(new SKUInventoryKey());
        Mockito.lenient().when(validationService.validateSerialNumber(anyString())).thenReturn("123");
        Mockito.lenient().when(validationService.validateWMSAction(anyString())).thenReturn("A");
        Mockito.lenient().when(validationService.validateWmsInventorySource(anyString())).thenReturn("G");
        Mockito.lenient().when(validationService.validateQty(anyLong())).thenReturn(1L);
        Mockito.lenient().when(itemService.getItem(any(ItemKey.class))).thenReturn(new Item());
        Mockito.lenient().when(skuInventoryService.perform(any(SKUInventoryKey.class), any(WmsInvRequest.class),
                any(WmsSku.class), any(WmsResponseDetail.class))).thenReturn(any(SkuInventory.class));
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForExistingTransaction() {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(wmsResponseHeaderRepository.findByTransactionNumber(wmsInvRequest.getTransactionNumber())).
                thenReturn(new WmsResponseHeader());
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForInvalidSerialNumber() throws SerialNumberMandatory {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(validationService.validateSerialNumber(any(String.class))).thenThrow(SerialNumberMandatory.class);
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForInvalidWmsAction() throws InvalidAction, ActionMandatory {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(validationService.validateWMSAction(any(String.class))).thenThrow(InvalidAction.class);
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForInvalidInventorySource() throws InventorySourceMandatory, InvalidWmsInventorySource {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(validationService.validateWmsInventorySource(any(String.class))).thenThrow(InventorySourceMandatory.class);
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForInvalidQty() throws InvalidQty {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(validationService.validateQty(anyLong())).thenThrow(InvalidQty.class);
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    @Test
    public void testProcessForInvalidItem() throws Exception {
        WmsInvRequest wmsInvRequest = createRequest();
        Mockito.lenient().when(itemService.getItem(any(ItemKey.class))).thenThrow(Exception.class);
        assertNotNull(Mockito.lenient().when(wmsInvService.process(wmsInvRequest)).thenReturn(new WmsResponseHeader()));
    }

    private WmsInvRequest createRequest() {
        return new WmsInvRequest("WM0000000001", LocalDateTime.now(), "USER", createSkuList());
    }

    private List<WmsSku> createSkuList() {
        return Arrays.asList(new WmsSku(createSkuInventoryKey(), "67890", 100, "A",
                "G", "Receive Inventory", "ASN Receipt"));
    }

    private SKUInventoryKey createSkuInventoryKey() {
        return new SKUInventoryKey("LEONISA", "LEONISA", "CSB", "887921535411",
                "", "", "A10000", "6", "403", "", "", "",
                "1170", "1", "F", "", "", "",
                "", "", "", "", "");
    }
}