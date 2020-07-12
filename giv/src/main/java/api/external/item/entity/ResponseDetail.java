package api.external.item.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents Response Detail of itemAPI
 * 
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-10-07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ResponseDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@JsonProperty("action")
	private String request;

	private char responseCode;

	private String responseId;
}