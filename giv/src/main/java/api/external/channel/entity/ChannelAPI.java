package api.external.channel.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** 
 * This class represents a Json payload (post request) with key value pairs of channelAPI
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-05-20
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAPI {
	@NotNull(message = "{TRANSACTION_NUMBER_MANDATORY}")
	@NotBlank(message = "{TRANSACTION_NUMBER_CANNOT_BE_BLANK}")
	@Column(unique = true)
	private String transactionNumber;

	private String channel;

	private String campaignCode;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;

	private String user;

	private List<ChannelAPISku> multipleSkus = new ArrayList<>();
}
