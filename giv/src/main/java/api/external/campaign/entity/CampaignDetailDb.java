package api.external.campaign.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import api.external.entity.BasicLogger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describes the campaign_detail_db entity.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2019-05-15
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CampaignDetailDb extends BasicLogger<String> implements Serializable, Persistable<CampaignDetailKey> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	CampaignDetailKey id;

	private int originalProtectQuantity;
	
	private int currentProtectQty;
	
	private int minimumQuantity;

	private int maximumQuantity;

	private String autoReplenish;	
	
	/*
	 * private String miscellaneousCharacterField1 = "";
	 * 
	 * private String miscellaneousCharacterField2 = "";
	 * 
	 * private String miscellaneousCharacterField3 = "";
	 * 
	 * private int miscellaneousNumericField1 = 0;
	 * 
	 * private int miscellaneousNumericField2 = 0;
	 * 
	 * private int miscellaneousNumericField3 = 0;
	 */
	
	@JsonIgnore
	@ManyToOne
	private CampaignHeaderDb campaignHeaderDb;
	
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