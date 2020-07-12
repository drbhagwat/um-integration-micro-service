package api.external.wms.validation;

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
import api.util.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * This class describes the wms validation service.
 *
 * @author : Sachin K
 * @version : 1.0
 * @since : 2019-10-11
 */
@Component
public class WmsValidationService {
    @Value("${EMPTY_SKU_LIST}")
    private String emptySkuList;

    @Value("${ACTION_MANDATORY}")
    private String actionMandatory;

    @Value("${WMS_QUERY_REQUEST_FOR}")
    private String[] wmsQueryRequestFor;

    @Value("${WMS_QUERY_TRANSACTIONS_ACTIONS}")
    private String[] wmsQueryTransactionsActions;

    @Value("${REQUEST_FOR_FIELD_CANNOT_BE_NULL_OR_BLANK}")
    private String requestForFieldCannotBeNullOrBlank;

    @Value("${INVALID_REQUEST_FOR}")
    private String invalidRequestFor;

    @Value("${INVENTORY_SOURCE_MANDATORY}")
    private String inventorySourceMandatory;

    @Value("${INVALID_WMS_INVENTORY_SOURCE}")
    private String invalidWmsInventorySource;

    @Value("${WMS_INVENTORY_SOURCE}")
    private String wmsInventorySource;

    @Value("${WMS_ACTIONS}")
    private String[] wmsActions;

    @Value("${INVALID_WMS_ACTION}")
    private String invalidWmsAction;

    @Value("${SERIAL_NUMBER_MANDATORY}")
    private String serialNumberMandatory;

    @Value("${INVALID_QTY}")
    private String invalidQty;

    @Autowired
    private Request request;

    @Value("${INVALID_QUERY_TRANSACTIONS_ACTION}")
    private String invalidQueryTransactionsAction;

    @Value("${WMS_QUERY_INVENTORY_DETAILS_ACTIONS}")
    private String[] wmsQueryInventoryDetailsActions;

    @Value("${CHANNEL_MANDATORY}")
    private String channelMandatory;

    public int validateSkuListSize(int listSize) throws SkuEmpty {
        if (listSize == 0) {
            throw new SkuEmpty(emptySkuList);
        }
        return listSize;
    }

    public String validateQueryTransactionsAction(String action) throws InvalidQueryTransactionsAction, ActionMandatory {
        if (StringUtils.isEmpty(action)) {
            throw new ActionMandatory(actionMandatory);
        } else if (action.equalsIgnoreCase(wmsQueryTransactionsActions[0]) || action.equalsIgnoreCase(wmsQueryTransactionsActions[1])
                || action.equalsIgnoreCase(wmsQueryTransactionsActions[2])) {
            return action;
        }
        throw new InvalidQueryTransactionsAction(invalidQueryTransactionsAction);
    }

    public String validateWMSAction(String action) throws ActionMandatory, InvalidAction {
        if (StringUtils.isEmpty(action)) {
            throw new ActionMandatory(actionMandatory);
        } else if (action.equalsIgnoreCase(wmsActions[0]) || action.equalsIgnoreCase(wmsActions[1])
                || action.equalsIgnoreCase(wmsActions[2]) || action.equalsIgnoreCase(wmsActions[3])) {
            return action;
        } else
            throw new InvalidAction(invalidWmsAction);
    }

    public String validateWmsInventorySource(String wmsInvSource) throws InventorySourceMandatory, InvalidWmsInventorySource {
        if (StringUtils.isEmpty(wmsInvSource)) {
            throw new InventorySourceMandatory(inventorySourceMandatory);
        } else if (!wmsInvSource.equals(wmsInventorySource)) {
            throw new InvalidWmsInventorySource(invalidWmsInventorySource);
        }
        return wmsInvSource;
    }

    public String validateSerialNumber(String serialNumber) throws SerialNumberMandatory {
        if (serialNumber == null)
            throw new SerialNumberMandatory(serialNumberMandatory);
        return serialNumber;
    }

    public Long validateQty(Long qty) throws InvalidQty {
        if (!request.isQtyGreaterThanZero(qty))
            throw new InvalidQty(invalidQty);
        return qty;
    }

    public String validateWMSQueryRequestFor(String requestFor) throws RequestForFieldIsMandatoryAndCannotBeBlank, InvalidRequestFor {
        if (StringUtils.isEmpty(requestFor)) {
            throw new RequestForFieldIsMandatoryAndCannotBeBlank(requestForFieldCannotBeNullOrBlank);
        } else if (requestFor.equalsIgnoreCase(wmsQueryRequestFor[0]) || requestFor.equalsIgnoreCase(wmsQueryRequestFor[1])) {
            return requestFor;
        }
        throw new InvalidRequestFor(invalidRequestFor);
    }

    public String validateQueryInventoryDetailsAction(String action) throws InvalidQueryInventoryDetailsAction, ActionMandatory {
        if (StringUtils.isEmpty(action)) {
            throw new ActionMandatory(actionMandatory);
        } else if (action.equalsIgnoreCase(wmsQueryInventoryDetailsActions[0]) || action.equalsIgnoreCase(wmsQueryInventoryDetailsActions[1])
                || action.equalsIgnoreCase(wmsQueryInventoryDetailsActions[2])) {
            return action;
        }
        throw new InvalidQueryInventoryDetailsAction(invalidQueryTransactionsAction);
    }

    public String validateChannel(String channel) throws ChannelIsMandatoryAndCannotBeBlank {
        if (StringUtils.isEmpty(channel))
            throw new ChannelIsMandatoryAndCannotBeBlank(channelMandatory);
        return channel;
    }
}