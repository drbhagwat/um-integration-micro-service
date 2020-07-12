package com.s3groupinc.gateway.api.entity.apidata;

import com.s3groupinc.gateway.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sachin Kulkarni
 * @date : 18-02-2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordByToken {
    private String token;
    private String password;
    private String confirmPassword;
    private User user;
}