package com.s3groupinc.gateway.api.entity.apidata;

import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.entity.Menu;
import com.s3groupinc.gateway.api.entity.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class represents a json payload with two key value pairs - used as response to login API.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonLoginResponse {
    private String authenticationSuccess; // true or false
    private String firstLogin; // NA or true or false
    private String apiUserOrAdmin; // NA or apiUser or or admin
    private String token;
    private String passwordExpired; // NA or true or false
//    private List<Permissions> permissionsList;
//    private List<Menu> menuList;
    private List<Groups> groupsList;
}