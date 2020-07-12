package api.external.channel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * This class represents Response Detail of channelAPI
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-10-15
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelAPIResponseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotBlank
    private char responseCode;

    @NotNull
    @NotBlank
    private String responseId;
}
