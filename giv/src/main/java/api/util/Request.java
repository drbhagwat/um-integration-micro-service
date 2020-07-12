package api.util;

import api.external.item.errors.InvalidAction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Validates action, inventorySource and Qty fields of all external APIs
 * 
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-07-15
 */
@Data
@NoArgsConstructor
@Component
public class Request {
    @Value("${INVENTORY_SOURCE}")
    private String[] inventorySource;

    @Value("${INVENTORY_SOURCE}")
    public void setInventorySource(String[] inventorySource) {
        this.inventorySource = inventorySource;
    }

    @Value("${WMS_ACTIONS}")
    private String[] wmsActions;

    @Value("${WMS_ACTIONS}")
    public void setWmsActions(String[] wmsActions) {
        this.wmsActions = wmsActions;
    }

    @Value("${MFG_ACTIONS}")
    private String[] mfgActions;

    @Value("${MFG_ACTIONS}")
    public void setActions(String[] mfgActions) {
        this.mfgActions = mfgActions;
    }

    @Value("${CHANNEL_ACTIONS}")
    private String[] channelActions;

    @Value("${CHANNEL_ACTIONS}")
    public void setChannelActions(String[] channelActions) {
        this.channelActions = channelActions;
    }

    @Value("${QUANTITY}")
    private int quantity;

    @Value("${QUANTITY}")
    public void setQuantity(int qty) {
        quantity = qty;
    }

    @Value("${WMS_INVENTORY_SOURCE}")
    private String wmsInventorySource;

    @Value("${MFG_INVENTORY_SOURCE}")
    private String mfgInventorySource;

    @Value("${INVALID_WMS_ACTION}")
    private String invalidWmsAction;

    @Value("${INVALID_MFG_ACTION}")
    private String invalidMfgAction;

    public boolean validateInventorySource(String invSource) {
        String uppercaseInventorySource = invSource.toUpperCase();
        return uppercaseInventorySource.equals(inventorySource[0]) || uppercaseInventorySource.equals(inventorySource[1]);
    }

    public String validateWMSAction(String action) throws InvalidAction {
        if (action.equalsIgnoreCase(wmsActions[0]) || action.equalsIgnoreCase(wmsActions[1])
                || action.equalsIgnoreCase(wmsActions[2]) || action.equalsIgnoreCase(wmsActions[3])) {
            return action;
        }
        throw new InvalidAction(invalidWmsAction);
    }

    public String validateMfgAction(String action) throws InvalidAction {
        if (action.equalsIgnoreCase(mfgActions[0]) || action.equalsIgnoreCase(mfgActions[1])
                || action.equalsIgnoreCase(mfgActions[2]) || action.equalsIgnoreCase(mfgActions[3])) {
            return action;
        }
        throw new InvalidAction(invalidMfgAction);
    }

    public boolean validateChannelAction(String action) {
        String uppercaseMode = action.toUpperCase();
        return uppercaseMode.equals(channelActions[0]) || uppercaseMode.equals(channelActions[1]);
    }

    public boolean isQtyGreaterThanZero(long quantity) {
        return quantity > 0;
    }

    public boolean validateWmsInventorySource(String wmsInvSource) {
        String uppercaseWmsInventorySource = wmsInvSource.toUpperCase();
        return uppercaseWmsInventorySource.equals(wmsInventorySource);
    }

    public boolean validateMfgInventorySource(String mfgInvSource) {
        String uppercaseMfgInventorySource = mfgInvSource.toUpperCase();
        return uppercaseMfgInventorySource.equals(mfgInventorySource);
    }
}