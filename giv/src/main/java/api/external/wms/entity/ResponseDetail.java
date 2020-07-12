package api.external.wms.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sachin Kulkarni
 * @date : 07-10-2019
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Character responseCode;

    private String responseId;
}
