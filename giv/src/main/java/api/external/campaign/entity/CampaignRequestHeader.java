package api.external.campaign.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to log CampaignRequestHeader of a campaign api call.
 * 
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-10-17
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({ "id", "createdUser", "createdDateTime", "lastUpdatedUser", "lastUpdatedDateTime" })
public class CampaignRequestHeader extends BasicLogger<String> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "transactionNumber", updatable = false, nullable = false)
	private String transactionNumber;

	private String channel;

	private String campaignCode;

	private LocalDate campaignStartDate;

	private LocalDate campaignEndDate;

	private String campaignActive;

	private String action;

	@Column(name = "usr")
	private String user = "";

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;

	@JsonProperty("campaignDetails")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="transactionNumber")
	private List<CampaignRequestDetail> campaignDetails = new ArrayList<>();
}