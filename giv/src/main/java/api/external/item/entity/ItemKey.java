package api.external.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This class represents Item primary Key.
 * 
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-11
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemKey implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String company;

	private String division;

	private String warehouse;

	private String skuBarcode;

	private String season;

	private String seasonYear;

	private String style;

	private String styleSfx;

	private String color;

	private String colorSfx;

	private String secDimension;

	private String quality;

	private String sizeRngeCode;

	private String sizeRelPosnIn;
}