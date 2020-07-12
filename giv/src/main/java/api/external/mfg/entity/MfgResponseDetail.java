package api.external.mfg.entity;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class MfgResponseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotBlank
    private Character responseCode;

    @NotNull
    @NotBlank
    private String responseId;
}
