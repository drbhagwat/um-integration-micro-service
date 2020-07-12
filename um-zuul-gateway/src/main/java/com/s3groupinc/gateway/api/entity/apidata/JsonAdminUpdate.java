package com.s3groupinc.gateway.api.entity.apidata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class represents a json payload (post request) with key value pairs - used for um admin.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonAdminUpdate {
    @NotNull(message = "{ADMIN_NAME_MANDATORY}")
    @NotBlank(message = "{ADMIN_NAME_CANNOT_BE_BLANK}")
    private String adminName;

    @NotNull(message = "{FULL_NAME_MANDATORY}")
    @NotBlank(message = "{FULL_NAME_CANNOT_BE_BLANK}")
    private String fullName;

    @NotNull(message = "{EMAIL_MANDATORY}")
    @NotBlank(message = "{EMAIL_CANNOT_BE_BLANK}")
    @Email
    private String email;

    @NotNull(message = "{PASSWORD_MANDATORY}")
    @NotBlank(message = "{PASSWORD_CANNOT_BE_BLANK}")
    private String password;

    @NotNull(message = "{API_ENABLED_SHOULD_BE_TRUE_OR_FALSE}")
    private Boolean apiEnabled;
}