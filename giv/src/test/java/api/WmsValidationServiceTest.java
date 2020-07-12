package api;

import api.external.errors.ActionMandatory;
import api.external.item.errors.InvalidAction;
import api.external.wms.errors.ChannelIsMandatoryAndCannotBeBlank;
import api.external.wms.errors.InvalidQty;
import api.external.wms.errors.InvalidQueryInventoryDetailsAction;
import api.external.wms.errors.InvalidQueryTransactionsAction;
import api.external.wms.errors.InvalidRequestFor;
import api.external.wms.errors.InvalidWmsInventorySource;
import api.external.wms.errors.InventorySourceMandatory;
import api.external.wms.errors.RequestForFieldIsMandatoryAndCannotBeBlank;
import api.external.wms.errors.SerialNumberMandatory;
import api.external.wms.errors.SkuEmpty;
import api.external.wms.validation.WmsValidationService;
import api.util.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : Sachin Kulkarni
 * @date : 06-02-2020
 */
@RunWith(MockitoJUnitRunner.class)
public class WmsValidationServiceTest {
    @InjectMocks
    private WmsValidationService wmsValidationService;

    @Mock
    private Request request;

    @Test
    public void testInvalidSkuListSize() {
        assertThrows(SkuEmpty.class, () -> wmsValidationService.validateSkuListSize(0));
    }

    @Test
    public void testValidSkuListSize() throws SkuEmpty {
        assertEquals(1, wmsValidationService.validateSkuListSize(1));
    }

    @Test
    public void testActionMandatoryExceptionForQueryTransactionsAction() {
        assertThrows(ActionMandatory.class, () -> wmsValidationService.validateQueryTransactionsAction(null));
    }

    @Test
    public void testValidQueryTransactionsAction() throws InvalidQueryTransactionsAction, ActionMandatory {
        String action = "QTR";
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryTransactionsActions", new String[]{"QTR", "QTA", "QTCH"});
        assertEquals("QTR", wmsValidationService.validateQueryTransactionsAction(action));
    }

    @Test
    public void testInvalidQueryTransactionsActionException() {
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryTransactionsActions", new String[]{"QTR", "QTA", "QTCH"});
        assertThrows(InvalidQueryTransactionsAction.class, () -> wmsValidationService.validateQueryTransactionsAction("ABC"));
    }

    @Test
    public void testActionMandatoryExceptionForWmsAction() {
        assertThrows(ActionMandatory.class, () -> wmsValidationService.validateWMSAction(null));
    }

    @Test
    public void testValidWmsAction() throws ActionMandatory, InvalidAction {
        String action = "A";
        ReflectionTestUtils.setField(wmsValidationService, "wmsActions", new String[]{"A", "S", "L", "U"});
        assertEquals("A", wmsValidationService.validateWMSAction(action));
    }

    @Test
    public void testInvalidWmsActionException() {
        ReflectionTestUtils.setField(wmsValidationService, "wmsActions", new String[]{"A", "S", "L", "U"});
        assertThrows(InvalidAction.class, () -> wmsValidationService.validateWMSAction("ABC"));
    }

    @Test
    public void testInventorySourceMandatoryExceptionForWmsInventorySource() {
        assertThrows(InventorySourceMandatory.class, () -> wmsValidationService.validateWmsInventorySource(null));
    }

    @Test
    public void testValidInventorySource() throws InvalidWmsInventorySource, InventorySourceMandatory {
        String inventorySource = "G";
        ReflectionTestUtils.setField(wmsValidationService, "wmsInventorySource", "G");
        assertEquals("G", wmsValidationService.validateWmsInventorySource(inventorySource));
    }

    @Test
    public void testInvalidWmsInventorySourceException() {
        ReflectionTestUtils.setField(wmsValidationService, "wmsInventorySource", "G");
        assertThrows(InvalidWmsInventorySource.class, () -> wmsValidationService.validateWmsInventorySource("A"));
    }

    @Test
    public void testValidSerialNumber() throws SerialNumberMandatory {
        String serialNumber = "123";
        assertEquals("123", wmsValidationService.validateSerialNumber(serialNumber));
    }

    @Test
    public void testSerialNumberMandatoryExceptionForSerialNumber() {
        assertThrows(SerialNumberMandatory.class, () -> wmsValidationService.validateSerialNumber(null));
    }

    @Test
    public void testValidQty() throws InvalidQty {
        long qty = 10L;
        Mockito.lenient().when(request.isQtyGreaterThanZero(qty)).thenReturn(true);
        assertEquals(10L, wmsValidationService.validateQty(qty));
    }

    @Test
    public void testInvalidQtyExceptionForQty() {
        Mockito.lenient().when(request.isQtyGreaterThanZero(-10L)).thenReturn(false);
        assertThrows(InvalidQty.class, () -> wmsValidationService.validateQty(-10L));
    }

    @Test
    public void testValidWMSQueryRequestFor() throws RequestForFieldIsMandatoryAndCannotBeBlank, InvalidRequestFor {
        String requestFor = "A";
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryRequestFor", new String[]{"A"});
        assertEquals("A", wmsValidationService.validateWMSQueryRequestFor(requestFor));
    }

    @Test
    public void testRequestForFieldIsMandatoryAndCannotBeBlankExceptionForWMSQueryRequestFor() {
        assertThrows(RequestForFieldIsMandatoryAndCannotBeBlank.class, () -> wmsValidationService.validateWMSQueryRequestFor(null));
    }

    @Test
    public void testInvalidRequestForExceptionForWMSQueryRequestFor() {
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryRequestFor", new String[]{"A","P"});
        assertThrows(InvalidRequestFor.class, () -> wmsValidationService.validateWMSQueryRequestFor("B"));
    }

    @Test
    public void testValidQueryInventoryDetailsAction() throws InvalidQueryInventoryDetailsAction, ActionMandatory {
        String action = "QIA";
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryInventoryDetailsActions", new String[]{"QI","QIA","QIC"});
        assertEquals("QIA", wmsValidationService.validateQueryInventoryDetailsAction(action));
    }

    @Test
    public void testActionMandatoryExceptionForQueryInventoryDetailsAction() {
        assertThrows(ActionMandatory.class, () -> wmsValidationService.validateQueryInventoryDetailsAction(null));
    }

    @Test
    public void testInvalidQueryInventoryDetailsActionExceptionForQueryInventoryDetailsAction() {
        ReflectionTestUtils.setField(wmsValidationService, "wmsQueryInventoryDetailsActions", new String[]{"QI","QIA","QIC"});
        assertThrows(InvalidQueryInventoryDetailsAction.class, () -> wmsValidationService.validateQueryInventoryDetailsAction("B"));
    }

    @Test
    public void testValidChannel() throws ChannelIsMandatoryAndCannotBeBlank {
        assertEquals("RETAIL", wmsValidationService.validateChannel("RETAIL"));
    }

    @Test
    public void testChannelIsMandatoryAndCannotBeBlankExceptionForChannel() {
        assertThrows(ChannelIsMandatoryAndCannotBeBlank.class, () -> wmsValidationService.validateChannel(null));
    }
}
