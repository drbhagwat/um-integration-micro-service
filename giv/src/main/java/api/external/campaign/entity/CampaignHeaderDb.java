package api.external.campaign.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describes the campaign_header_db entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CampaignHeaderDb extends BasicLogger<String> implements Serializable, Persistable<CampaignHeaderKey> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	CampaignHeaderKey id;
	
	private LocalDate startDate;

	private LocalDate endDate;

	@Column(name = "is_active")
	private String active;

//	private String miscellaneousCharacterField1 = "";
//
//	private String miscellaneousCharacterField2 = "";
//
//	private String miscellaneousCharacterField3 = "";
//
//	private int miscellaneousNumericField1 = 0;
//
//	private int miscellaneousNumericField2 = 0;
//
//	private int miscellaneousNumericField3 = 0;
	
	private String runningActive;
	
	private String action;
	
	@Column(name = "usr")
	private String user = "";

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateTimeStamp;
	
	@JsonIgnore
	@OneToMany(mappedBy = "campaignHeaderDb", cascade = CascadeType.ALL)
	private List<CampaignDetailDb> campaignDbDetails;

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