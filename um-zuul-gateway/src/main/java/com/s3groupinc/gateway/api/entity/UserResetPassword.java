package com.s3groupinc.gateway.api.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class represents UserResetPassword
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2020-02-12
 */

@Data
public class UserResetPassword {
    @NotNull(message = "{USER_NAME_MANDATORY}")
    @NotBlank(message = "{USER_NAME_CANNOT_BE_BLANK}")
    String username;

    @NotNull(message = "{PASSWORD_MANDATORY}")
    @NotBlank(message = "{PASSWORD_CANNOT_BE_BLANK}")
    String password;

    @NotNull(message = "{PASSWORD_MANDATORY}")
    @NotBlank(message = "{PASSWORD_CANNOT_BE_BLANK}")
    String newPassword;
}