package api.external.campaign.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents response detail of a campaign api call.
 * 
 * @author : Anghulakshmi B
 * @date : 2019-10-18
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	private char responseCode;

	@NotNull
	@NotBlank
	private String responseId;
}