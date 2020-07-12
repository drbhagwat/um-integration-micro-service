package com.s3groupinc.gateway.api.entity.apidata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author : Sachin Kulkarni
 * @date : 17-02-2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassword {
    @Email
    @NotNull(message = "{EMAIL_MANDATORY}")
    @NotBlank(message = "{EMAIL_CANNOT_BE_BLANK}")
    private String emailId;
}