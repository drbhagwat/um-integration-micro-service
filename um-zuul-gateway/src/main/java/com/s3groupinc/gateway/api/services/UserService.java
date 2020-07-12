package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.email.EmailService;
import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.entity.Menu;
import com.s3groupinc.gateway.api.entity.PasswordResetToken;
import com.s3groupinc.gateway.api.entity.Permissions;
import com.s3groupinc.gateway.api.entity.Role;
import com.s3groupinc.gateway.api.entity.User;
import com.s3groupinc.gateway.api.entity.UserResetPassword;
import com.s3groupinc.gateway.api.entity.apidata.ForgotPassword;
import com.s3groupinc.gateway.api.entity.apidata.ResetPasswordByToken;
import com.s3groupinc.gateway.api.errors.GroupNotFound;
import com.s3groupinc.gateway.api.errors.InvalidPassword;
import com.s3groupinc.gateway.api.errors.InvalidPasswordSpace;
import com.s3groupinc.gateway.api.errors.MenuNotFound;
import com.s3groupinc.gateway.api.errors.PasswordMatches;
import com.s3groupinc.gateway.api.errors.PasswordMaximumLengthExceeded;
import com.s3groupinc.gateway.api.errors.TokenExpired;
import com.s3groupinc.gateway.api.errors.UserAlreadyExists;
import com.s3groupinc.gateway.api.errors.UserDoesNotMatchWithRequestedToken;
import com.s3groupinc.gateway.api.errors.UserNotFound;
import com.s3groupinc.gateway.api.repo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides CRUD services for User Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private MenuService menuService;

    private static final int WORK_LOAD = 6;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*]).{6}$";

    /**
     * To check whether the username is present or not in the user
     *
     * @param username - field of the user
     * @return - on success, returns the found user.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userDetails = userRepository.findOneByUsername(username);
        if (userDetails.isPresent()) {
            return userDetails.get();
        }
        throw new UserNotFound("No User");
    }

    /**
     * Get all users.
     *
     * @return - on success, returns the list of all users.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a specific user with id(primary key).
     *
     * @param id - PK of the user.
     * @return - on success, returns the found user.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public User getUser(int id) throws Exception {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new Exception("User id " + id + " is not found.");
        }
        return user.get();
    }

    /**
     * Get a specific user with matched userEmail.
     *
     * @param email - field of the user
     * @return - on success, returns the found user.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public User getUserByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findOneByEmail(email);

        if (user.isEmpty()) {
            throw new Exception("User email " + email + " is not found.");
        }
        return user.get();
    }

    /**
     * Adds a user to the database
     *
     * @param users - the user to be added to the database.
     * @return - on success, returns the saved user.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public User addUser(User users) throws Exception {
        String userName = users.getUsername();
        String name = users.getFullName();
        String email = users.getEmail().toLowerCase();
        String password = users.getPassword();
        boolean apiEnabled = users.isApiEnabled();

        if (password == null) {
            throw new Exception("The Password key is missing in the Json input. Please provide the password key with a value");
        }

        if (password.isBlank()) {
            throw new Exception("The value of the Password key in the Json input is blank. Please provide a non-blank value for the Password key ");
        }

        Optional<User> existingUser = userRepository.findByUsernameAndEmail(userName, email);

        if (existingUser.isPresent()) {
            throw new UserAlreadyExists("User already exists with this username and email.");
        }

        Optional<User> username = userRepository.findOneByUsername(userName);

        if (username.isPresent()) {
            throw new UserAlreadyExists("User name with " + userName + " already exists.");
        }

        Optional<User> emailId = userRepository.findOneByEmail(email);

        if (emailId.isPresent()) {
            throw new Exception("Email with " + email + " already exists.");
        }

        User newUser = new User();
        // save the name, name and email you got from JSON
        newUser.setUsername(userName.trim());
        newUser.setFullName(name.trim());
        newUser.setEmail(email.trim());
        // make the user active by default.
        newUser.setActive(true);
        // enable or disable API access as specified
        newUser.setApiEnabled(apiEnabled);
        // encode that password so there is some security
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        List<Groups> newGroup = new ArrayList<>();
        List<Permissions> newPermission = new ArrayList<>();
        List<Menu> newMenu = new ArrayList<>();
        newUser.setRoles(Collections.singletonList(new Role("ROLE_USER")));

        if (!users.getGroups().isEmpty()) {
            for (Groups group : users.getGroups()) {
                List<Groups> groups = groupsRepository.findByGroupName(group.getGroupName());
                if (groups.isEmpty()) {
                    throw new GroupNotFound("Group name with " + group.getGroupName() + " is not found.");
                }
                group.setId(null);
                newGroup.add(group);

                if (!group.getPermissionsList().isEmpty()) {
                    for (Permissions permission : group.getPermissionsList()) {
                        List<Permissions> permissions = permissionsRepository.findOneByPermissionName(permission.getPermissionName());
                        if (permissions.isEmpty()) {
                            throw new Exception("Permission name with " + permission.getPermissionName() + " is not found.");
                        }
                        permission.setId(null);
                        newPermission.add(permission);
                    }
                }

                if (!group.getMenuList().isEmpty()) {
                    for (Menu menu : group.getMenuList()) {
                        List<Menu> menus = menuRepository.findOneByName(menu.getName());
                        if (menus.isEmpty()) {
                            throw new MenuNotFound("Menu name with " + menu.getName() + " is not found.");
                        }
                        menu.setComponentName(null);
                        menu.setPath(null);
                        newMenu.add(menu);
                    }
                }

                newGroup = newGroup.stream().distinct().collect(Collectors.toList());
                newPermission = newPermission.stream().distinct().collect(Collectors.toList());
                newMenu = newMenu.stream().distinct().collect(Collectors.toList());
                newUser.setGroups(newGroup);
            }
        }

        newUser.setFirstLogin(true);
        LocalDate expiredDate = LocalDate.now().plusDays(60);
        newUser.setExpiredDate(expiredDate);

        // finally save the user
        return userRepository.save(newUser);
    }

    /**
     * Update a reset password in the user
     *
     * @param userResetPassword - jsonUser, which is a json class to reset the password
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public String resetPassword(UserResetPassword userResetPassword) throws UserNotFound, PasswordMaximumLengthExceeded,
            InvalidPassword, PasswordMatches, InvalidPasswordSpace {
        Optional<User> user = userRepository.findOneByUsername(userResetPassword.getUsername());
        User existingUser = user.orElseThrow(() -> new UserNotFound("Invalid User"));
        String newPassword = userResetPassword.getNewPassword();

        if (newPassword.contains(" ")) {
            throw new InvalidPasswordSpace("Password should not contain any space.");
        }
        if (newPassword.length() > WORK_LOAD) {
            throw new PasswordMaximumLengthExceeded("Password length should have 6 in length");
        }

        if (!newPassword.matches(PASSWORD_PATTERN)) {
            throw new InvalidPassword("Password should contain at least one digit(0-9), one special character (!,@,#,$,%,^,&,*) and lowercase letter(a-z).");
        }

        if (BCrypt.checkpw(newPassword, existingUser.getPassword())) {
            throw new PasswordMatches("Old Password and New Password are same. Please use different one");
        }
        existingUser.setNewPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);

        return "Password Reset Successful";
    }

    public Boolean passwordExpiry(String username) throws UserNotFound {
        Optional<User> existingUser = userRepository.findOneByUsername(username);
        User user = existingUser.orElseThrow(() -> new UserNotFound("User not found"));
        LocalDate expiredDate = user.getExpiredDate();

        return expiredDate.compareTo(LocalDate.now()) >= 0;
    }

    /**
     * Forgot Password
     *
     * @param forgotPassword - jsonUser, which is a json class to forgot the password
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public String forgotPassword(ForgotPassword forgotPassword) throws UserNotFound, MessagingException {
        String emailId = forgotPassword.getEmailId();
        Optional<User> existingUser = userRepository.findOneByEmail(emailId);
        User user = existingUser.orElseThrow(() -> new UserNotFound("User not found"));
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDate(30);
        passwordResetTokenRepository.save(passwordResetToken);
        String url = "http://localhost:4200/login/resetpass?token=" + passwordResetToken.getToken();
        emailService.sendHtmlMail(emailId, url);
        return "Successfully sent an email to " + emailId;
    }

    /**
     * Reset Password By Token in user
     *
     * @param resetPasswordByToken - jsonUser, which is a json class to reset the password by token
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public String resetPasswordByToken(ResetPasswordByToken resetPasswordByToken) throws PasswordMaximumLengthExceeded,
            InvalidPassword, TokenExpired, UserDoesNotMatchWithRequestedToken, UserNotFound, InvalidPasswordSpace {
        String token = resetPasswordByToken.getToken();
        PasswordResetToken existingToken = passwordResetTokenRepository.findByToken(token);

        if (existingToken != null) {
            existingToken = Optional.of(existingToken).filter(PasswordResetToken::isExpired).
                    orElseThrow(() -> new TokenExpired("Token already expired"));
            User existingTokenUser = existingToken.getUser();
            String existingTokenUserName = existingTokenUser.getUsername();
            User resetPasswordUser = resetPasswordByToken.getUser();
            String resetPasswordUserName = resetPasswordUser.getUsername();

            if (!existingTokenUserName.equals(resetPasswordUserName)) {
                throw new UserDoesNotMatchWithRequestedToken("User does not match with requested token");
            }

            String newPassword = Optional.of(resetPasswordByToken.getPassword()).filter(s -> s.matches(PASSWORD_PATTERN)).
                    orElseThrow(() -> new InvalidPassword("Invalid Password"));

            if (newPassword.length() > WORK_LOAD) {
                throw new PasswordMaximumLengthExceeded("Password length should not exceed more than 6 characters");
            }

            if (newPassword.contains(" ")) {
                throw new InvalidPasswordSpace("Password should not contain any space.");
            }

            Optional<User> existingUser = userRepository.findOneByUsername(resetPasswordUserName);
            User user = existingUser.orElseThrow(() -> new UserNotFound("User not found"));
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return "Password reset successful for " + resetPasswordUserName;
        }
        return "Password reset Unsuccessful";
    }

    /**
     * Updates an existing user in the database with matched userEmail.
     *
     * @param users - the user to be updated in the database.
     * @return - on success, returns the updated user.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void updateUserByEmail(User users) throws Exception {
        String userName = users.getUsername();
        String name = users.getFullName();
        String email = users.getEmail().toLowerCase();
        String password = users.getPassword();
        boolean apiEnabled = users.isApiEnabled();
        Optional<User> user = userRepository.findByUsernameAndEmail(userName, email);

        if (user.isEmpty()) {
            throw new UserNotFound("Unable to find email for the existing username with the given userName.");
        }

        List<Groups> newGroup = new ArrayList<>();
        List<Permissions> newPermission = new ArrayList<>();
        List<Menu> newMenu = new ArrayList<>();
        User updatedUser = user.get();

        List<Groups> userGroups = updatedUser.getGroups();
        for (Groups group : userGroups) {
            for (Menu menu : group.getMenuList()) {
                menuService.deleteMenusWithId(menu.getId());
            }
            for (Permissions permission : group.getPermissionsList()) {
                permissionsService.deletePermissions(permission.getId());
            }
            groupsService.deleteGroups(group.getId());
        }

        if (!users.getGroups().isEmpty()) {
            for (Groups group : users.getGroups()) {
                List<Groups> groups = groupsRepository.findByGroupName(group.getGroupName());
                if (groups.isEmpty()) {
                    throw new GroupNotFound("Group name with " + group.getGroupName() + " is not found.");
                }
                group.setId(null);
                newGroup.add(group);

                for (Permissions permission : group.getPermissionsList()) {
                    List<Permissions> permissions = permissionsRepository.findOneByPermissionName(permission.getPermissionName());
                    if (permissions.isEmpty()) {
                        throw new Exception("Permission name with " + permission.getPermissionName() + " is not found.");
                    }
                    permission.setId(null);
                    newPermission.add(permission);
                }
                newPermission = newPermission.stream().distinct().collect(Collectors.toList());
                group.setPermissionsList(newPermission);

                for (Menu menu : group.getMenuList()) {
                    List<Menu> menus = menuRepository.findOneByName(menu.getName());
                    if (menus.isEmpty()) {
                        throw new Exception("Menu name with " + menu.getName() + " is not found.");
                    }
                    menu.setComponentName(null);
                    menu.setPath(null);
                    newMenu.add(menu);
                }
                newMenu = newMenu.stream().distinct().collect(Collectors.toList());
                group.setMenuList(newMenu);
            }
            newGroup = newGroup.stream().distinct().collect(Collectors.toList());
            updatedUser.setGroups(newGroup);
        }
        updatedUser.setUsername(userName.trim());
        updatedUser.setFullName(name.trim());
        updatedUser.setEmail(email.trim());
        // make the user active by default.
        updatedUser.setActive(true);
        // enable or disable API access as specified
        updatedUser.setApiEnabled(apiEnabled);
        if (password != null && (!password.isBlank())) {
            // encode that password so there is some security
            String encodedPassword = passwordEncoder.encode(password);
            updatedUser.setPassword(encodedPassword);
        }
        userRepository.save(updatedUser);
    }

    /**
     * Deletes the role, group, permission and menu for a user with matched userEmail.
     *
     * @param email - field of the user
     * @return - on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void deleteUserByEmail(String email) throws Exception {
        User user = getUserByEmail(email);
        List<Role> roles = user.getRoles();
        List<Groups> groups = user.getGroups();

        for (Role role : roles) {
            roleService.deleteRole(role.getId());
        }
        for (Groups group : groups) {
            for (Permissions permission : group.getPermissionsList()) {
                permissionsService.deletePermissions(permission.getId());
            }
            for (Menu menu : group.getMenuList()) {
                menuService.deleteMenusWithId(menu.getId());
            }
            groupsService.deleteGroups(group.getId());
        }
        userRepository.delete(user);
    }
}