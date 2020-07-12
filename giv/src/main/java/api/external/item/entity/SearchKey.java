package api.external.item.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchKey {
	@NotBlank
	@NotNull
	@JsonProperty("Company")
	private String company;

	@NotBlank
	@NotNull
	@JsonProperty("Division")
	private String division;

	@NotBlank
	@NotNull
	@JsonProperty("Warehouse")
	private String warehouse;

	@NotNull
	@JsonProperty("SkuBarcode")
	private String skuBarcode;
}