package api.external.channel.errors;

import org.springframework.beans.factory.annotation.Autowired;

import api.external.inventory.entity.SkuInventory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllocatedQtyZero extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SkuInventory skuInventory;
	
	String responseQty = null;
	String onHandQty = null;
	String allocatedQty = null;
	String protectedQty = null;
	String lockedQty = null;
	String availableQty = null;
	

	public AllocatedQtyZero(String exception, String responseQty, String onHandQty, String allocatedQty, String protectedQty, String lockedQty, String availableQty) {
    super(exception);
    this.responseQty = responseQty;
    this.onHandQty = onHandQty;
    this.allocatedQty = allocatedQty;
    this.protectedQty = protectedQty;
    this.lockedQty = lockedQty;
    this.availableQty = availableQty;


  }
}