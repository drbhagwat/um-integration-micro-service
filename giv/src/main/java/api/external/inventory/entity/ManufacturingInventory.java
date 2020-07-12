package api.external.inventory.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;

import api.external.entity.BasicLogger;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents manufacturing inventory entity
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-06-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ManufacturingInventory extends BasicLogger<String> implements Serializable, Persistable<ManufacturingInventoryKey> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@JsonUnwrapped
	ManufacturingInventoryKey id;
	
	private String customerOrderNumber;
	
	private String productionOrderNumber;
	
	private String purchaseOrderNumber;
	
	private double onHandQuantity;

	private double protectedQuantity;

	private double lockedQuantity;

	private double allocatedQuantity;

	private double availableQuantity;

	private String serialNumber;

	private String miscellaneousCharacterField1;

	private String miscellaneousCharacterField2;

	private String miscellaneousCharacterField3;

	private int miscellaneousNumericField1;

	private int miscellaneousNumericField2;

	private int miscellaneousNumericField3;

	private @Transient boolean isNew = true;

	@Override
	public boolean isNew() {
		return isNew;
	}

	@PostPersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}
}