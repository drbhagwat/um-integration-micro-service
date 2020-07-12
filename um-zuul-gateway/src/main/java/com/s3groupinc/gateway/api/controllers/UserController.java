package com.s3groupinc.gateway.api.controllers;

import com.google.gson.Gson;
import com.s3groupinc.gateway.api.entity.User;
import com.s3groupinc.gateway.api.entity.UserResetPassword;
import com.s3groupinc.gateway.api.entity.apidata.ForgotPassword;
import com.s3groupinc.gateway.api.entity.apidata.ResetPasswordByToken;
import com.s3groupinc.gateway.api.errors.*;
import com.s3groupinc.gateway.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

/**
 * Manages CRUD (Create Read Update Delete) operations of um user.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Get all users.
     *
     * @return - on success, returns the list of all users.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/users")
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Get a specific user with matched userEmail.
     *
     * @param email - field of the user
     * @return - on success, returns the found user.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/users/{email}")
    public User getUserByEmail(@PathVariable String email) throws Exception {
        return userService.getUserByEmail(email);
    }

    /**
     * Add a user
     *
     * @param user - jsonUser, which is a json class to add the user
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/users")
    public ResponseEntity<String> registration(@RequestBody @Valid User user) {

        try {
            User savedPassword = userService.addUser(user);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Gson gson = new Gson();
        String message = "User with email " + user.getEmail() + " is successfully added.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Update a user
     *
     * @param user - jsonUser, which is a json class to update the existing user
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PutMapping("/users")
    public ResponseEntity<String> updateUsersByEmail(@RequestBody User user) throws Exception {
        userService.updateUserByEmail(user);
        Gson gson = new Gson();
        String message = "User with email " + user.getEmail() + " is successfully updated.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Update a reset password in the user
     *
     * @param userResetPassword - jsonUser, which is a json class to reset the password
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody UserResetPassword userResetPassword) throws UserNotFound,
            InvalidPassword, PasswordMaximumLengthExceeded, PasswordMatches, InvalidPasswordSpace {
        String message = userService.resetPassword(userResetPassword);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Forgot Password
     *
     * @param forgotPassword - jsonUser, which is a json class to forgot the password
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/forgotpassword")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPassword forgotPassword) throws UserNotFound,
            MessagingException {
        String message = userService.forgotPassword(forgotPassword);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Reset Password By Token in user
     *
     * @param resetPasswordByToken - jsonUser, which is a json class to reset the password by token
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */

    @PostMapping("/resetpasswordbytoken")
    public ResponseEntity<String> resetPasswordByToken(@RequestBody ResetPasswordByToken resetPasswordByToken)
            throws InvalidPassword, PasswordMaximumLengthExceeded, UserNotFound, TokenExpired,
            UserDoesNotMatchWithRequestedToken, InvalidPasswordSpace {
        String message = userService.resetPasswordByToken(resetPasswordByToken);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Delete an existing user with matched userEmail
     *
     * @param email - field of the user.
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @DeleteMapping("/users/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) throws Exception {
        userService.deleteUserByEmail(email);
        Gson gson = new Gson();
        String message = "User with email " + email + " is successfully deleted.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }
}