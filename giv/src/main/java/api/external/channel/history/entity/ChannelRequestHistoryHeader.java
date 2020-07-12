package api.external.channel.history.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-17
 * 
 * This class represents ChannelRequestHistoryHeader entity
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChannelRequestHistoryHeader extends BasicLogger<String> {

	@Id
	@Column(name = "transactionNumber", updatable = false, nullable = false)
	private String transactionNumber;

	private String channel;
	
	private String campaignCode;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;

	@Column(name = "usr")
	private String user;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "transactionNumber")
	private List<ChannelRequestHistorySku> multipleSkus = new ArrayList<>();
}
