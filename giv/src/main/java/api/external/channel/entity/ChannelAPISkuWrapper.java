package api.external.channel.entity;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the wrapper class of the ChannelAPISku
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since	2019-05-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAPISkuWrapper {
	private List<ChannelAPISku> channelAPISkuWrapper;
}
