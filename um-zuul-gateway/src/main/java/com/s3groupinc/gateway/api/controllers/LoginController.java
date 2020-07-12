package com.s3groupinc.gateway.api.controllers;

import com.s3groupinc.gateway.api.entity.apidata.JsonChangePassword;
import com.s3groupinc.gateway.api.entity.apidata.JsonLogin;
import com.s3groupinc.gateway.api.entity.apidata.JsonLoginResponse;
import com.s3groupinc.gateway.api.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages login of a giv user.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    // @Autowired
    // private UserService userDetailsService;

    /**
     * @param jsonLogin: two key value pairs - userName and password
     * @return - on successful authentication, it returns true, otherwise false.
     * @throws Exception
     */
    @PostMapping("/login")
    public JsonLoginResponse login(@RequestBody JsonLogin jsonLogin) throws Exception {

        return (loginService.login1(jsonLogin));
    }

    /**
     * This method is to change the password
     *
     * @param jsonChangePassword: three key value pairs - userName, oldPassword and newPassword
     * @return - on successful authentication, it returns true, otherwise false.
     */
    @PostMapping("/changepassword")
    public boolean changePassword(@RequestBody JsonChangePassword jsonChangePassword) {
        return (loginService.changePassword(jsonChangePassword));
    }

    // private void authenticate(String username, String password) throws Exception {
    // 	try {
    // 		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    // 	} catch (DisabledException e) {
    // 		throw new Exception("USER_DISABLED", e);
    // 	} catch (BadCredentialsException e) {
    // 		throw new Exception("INVALID_CREDENTIALS", e);
    // 	}
    // }
}