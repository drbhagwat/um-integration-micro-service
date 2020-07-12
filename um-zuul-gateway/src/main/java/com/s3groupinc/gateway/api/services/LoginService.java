package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.config.jwtutil.JwtTokenUtil;
import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.entity.User;
import com.s3groupinc.gateway.api.entity.apidata.JsonChangePassword;
import com.s3groupinc.gateway.api.entity.apidata.JsonLogin;
import com.s3groupinc.gateway.api.entity.apidata.JsonLoginResponse;
import com.s3groupinc.gateway.api.errors.UserNotFound;
import com.s3groupinc.gateway.api.repo.UserRepository;
import java.util.Hashtable;
import java.util.Optional;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides services for Login Controller.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-05-01
 */
@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.enabled}")
    private String ldapEnabled;

    @Value("${SECURITY_AUTHENTICATION}")
    private String securityAuthentication;

    @Value("${USER_CONTEXT}")
    private String userContext;

    @Value("${INITIAL_CONTEXT_FACTORY}")
    private String initialContextFactory;

    String userName = null;
    String fullName = null;
    String email = null;
    String password = null;

    /**
     * This method is to validate first login or not
     *
     * @param jsonLogin: two key value pairs - userName and password
     * @return - on successful authentication, it returns true, otherwise false.
     */
    public JsonLoginResponse login1(JsonLogin jsonLogin) throws Exception {
        JsonLoginResponse jsonLoginResponse = new JsonLoginResponse();
        userName = jsonLogin.getUserName();
        password = jsonLogin.getPassword();

        // If Ldap is enabled, authentication process should be done via LDAP server
        if (Boolean.parseBoolean(ldapEnabled)) {

            DirContext ctx = null;
            try {
                // creating environment for initial context
                Hashtable<String, String> env = new Hashtable<String, String>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
                env.put(Context.PROVIDER_URL, ldapUrls);
                env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
                env.put(Context.SECURITY_PRINCIPAL, "uid=" + userName + "," + userContext);
                env.put(Context.SECURITY_CREDENTIALS, password);

                // Create the initial context
                ctx = new InitialDirContext(env);

                System.out.println("Authenticated: " + (ctx != null));

                // get more attributes about this user
                SearchControls scs = new SearchControls();
                scs.setSearchScope(SearchControls.SUBTREE_SCOPE);

                NamingEnumeration nes = ctx.search(userContext, "uid=" + userName, scs);

                if (nes.hasMore()) {
                    Attributes attrs = ((SearchResult) nes.next()).getAttributes();

                    // get userName, fullName and email from the LDAP server
                    userName = attrs.get("uid").get().toString();
                    fullName = attrs.get("cn").get().toString();
                    email = attrs.get("mail").get().toString();
                    //password = attrs.get("userPassword").get().toString();
                }

                Optional<User> user = userRepository.findOneByEmail(email);
                User usr = null;

                // if user is empty add the authenticated LDAP user to our database
                if (user.isEmpty()) {

                    User newUser = new User();
                    newUser.setUsername(userName);
                    newUser.setFullName(fullName);
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    newUser.setApiEnabled(true);

                    usr = userService.addUser(newUser);
                    jsonLoginResponse.setFirstLogin("true");
                } else {
                    usr = user.get();
                    jsonLoginResponse.setFirstLogin("false");

//                    for (Groups groups : usr.getGroups()) {
//                        jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                        jsonLoginResponse.setMenuList(groups.getMenuList());
//                    }

                    Boolean passwordExpiry = userService.passwordExpiry(userName);

                    if (!passwordExpiry) {
                        jsonLoginResponse.setApiUserOrAdmin("NA");
                        jsonLoginResponse.setFirstLogin("NA");
                        jsonLoginResponse.setAuthenticationSuccess("false");
                        jsonLoginResponse.setToken("NA");
                        jsonLoginResponse.setPasswordExpired("true");

//                        for (Groups groups : usr.getGroups()) {
//                            jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                            jsonLoginResponse.setMenuList(groups.getMenuList());
//                        }
                    jsonLoginResponse.setGroupsList(usr.getGroups());
                        return jsonLoginResponse;
                    }
                }

                jsonLoginResponse.setAuthenticationSuccess("true");
                jsonLoginResponse.setApiUserOrAdmin("apiUser");
                jsonLoginResponse.setPasswordExpired("false");

                // generate token for authenticated user
                final String token = jwtTokenUtil.generateToken(usr);
                jsonLoginResponse.setToken(token);

//                for (Groups groups : usr.getGroups()) {
//                    jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                    jsonLoginResponse.setMenuList(groups.getMenuList());
//                }
                jsonLoginResponse.setGroupsList(usr.getGroups());

                usr.setFirstLogin(false);
                userRepository.save(usr);
                return jsonLoginResponse;
            } catch (NamingException e) {
                jsonLoginResponse.setAuthenticationSuccess("false");
                jsonLoginResponse.setFirstLogin("NA");
                jsonLoginResponse.setApiUserOrAdmin("NA");
                jsonLoginResponse.setToken("NA");
                jsonLoginResponse.setPasswordExpired("NA");
                return jsonLoginResponse;
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                if (ctx != null)
                    try {
                        ctx.close();
                    } catch (NamingException ex) {
                    }
            }
        } else {
            // Normal spring authentication with JWT
            return login(jsonLogin);
        }
    }

    /**
     * This method is to validate first login or not
     *
     * @param jsonLogin: two key value pairs - userName and password
     * @return - on successful authentication, it returns true, otherwise false.
     */
    public JsonLoginResponse login(JsonLogin jsonLogin) throws UserNotFound {
        JsonLoginResponse jsonLoginResponse = new JsonLoginResponse();
        String userName = jsonLogin.getUserName();
        String password = jsonLogin.getPassword();

        Optional<User> user = userRepository.findOneByUsername(userName);

        if (user.isEmpty()) {
            jsonLoginResponse.setAuthenticationSuccess("false");
            jsonLoginResponse.setFirstLogin("NA");
            jsonLoginResponse.setApiUserOrAdmin("NA");
            jsonLoginResponse.setToken("NA");
            jsonLoginResponse.setPasswordExpired("NA");
            return jsonLoginResponse;
        }

        User usr = user.get();
        String dbPassword = usr.getPassword();
        boolean result = BCrypt.checkpw(password, dbPassword);

        if (!result) {
            jsonLoginResponse.setAuthenticationSuccess("false");
            jsonLoginResponse.setFirstLogin("NA");
            jsonLoginResponse.setApiUserOrAdmin("NA");
            jsonLoginResponse.setToken("NA");
            jsonLoginResponse.setPasswordExpired("NA");

//            for (Groups groups : usr.getGroups()) {
//                jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                jsonLoginResponse.setMenuList(groups.getMenuList());
//            }
            jsonLoginResponse.setGroupsList(usr.getGroups());
            return jsonLoginResponse;
        }

        if (!usr.isActive()) {
            jsonLoginResponse.setAuthenticationSuccess("false");
            jsonLoginResponse.setFirstLogin("NA");
            jsonLoginResponse.setApiUserOrAdmin("NA");
            jsonLoginResponse.setToken("NA");
            jsonLoginResponse.setPasswordExpired("NA");

//            for (Groups groups : usr.getGroups()) {
//                jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                jsonLoginResponse.setMenuList(groups.getMenuList());
//            }
            jsonLoginResponse.setGroupsList(usr.getGroups());

            return jsonLoginResponse;
        }

        boolean passwordExpiry = userService.passwordExpiry(userName);

        if (!passwordExpiry) {
            jsonLoginResponse.setApiUserOrAdmin("NA");
            jsonLoginResponse.setFirstLogin("NA");
            jsonLoginResponse.setAuthenticationSuccess("false");
            jsonLoginResponse.setToken("NA");
            jsonLoginResponse.setPasswordExpired("true");

//            for (Groups groups : usr.getGroups()) {
//                jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//                jsonLoginResponse.setMenuList(groups.getMenuList());
//            }
            jsonLoginResponse.setGroupsList(usr.getGroups());

            return jsonLoginResponse;
        }

        final String token = jwtTokenUtil.generateToken(usr);
        jsonLoginResponse.setToken(token);

        jsonLoginResponse.setAuthenticationSuccess("true");
        jsonLoginResponse.setPasswordExpired("false");

        if (usr.isFirstLogin()) {
            jsonLoginResponse.setFirstLogin("true");
        } else {
            jsonLoginResponse.setFirstLogin("false");
        }

        if (usr.isAdmin()) {
            jsonLoginResponse.setApiUserOrAdmin("admin");
        } else {
            jsonLoginResponse.setApiUserOrAdmin("apiUser");
        }
        usr.setFirstLogin(false);

//        for (Groups groups : usr.getGroups()) {
//            jsonLoginResponse.setPermissionsList(groups.getPermissionsList());
//            jsonLoginResponse.setMenuList(groups.getMenuList());
//        }
        jsonLoginResponse.setGroupsList(usr.getGroups());

        userRepository.save(usr);
        return jsonLoginResponse;
    }

    /**
     * This method is to change the password
     *
     * @param jsonChangePassword: three key value pairs - userName, oldPassword and newPassword
     * @return - on successful authentication, it returns true, otherwise false.
     */
    public boolean changePassword(JsonChangePassword jsonChangePassword) {
        String userName = jsonChangePassword.getUserName();
        String oldPassword = jsonChangePassword.getOldPassword();
        Optional<User> user = userRepository.findOneByUsername(userName);
        User usr = new User();

        if (user.isEmpty()) {
            return false;
        } else {
            usr = user.get();
        }
        String dbPassword = usr.getPassword();
        boolean result = BCrypt.checkpw(oldPassword, dbPassword);

        if (!result) {
            return false;
        }

        if (!usr.isActive()) {
            return false;
        }
        String newPassword = jsonChangePassword.getNewPassword();
        // encode that password so there is security
        String encodedPassword = passwordEncoder.encode(newPassword);
        usr.setPassword(encodedPassword);
        userRepository.save(usr);
        return true;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}