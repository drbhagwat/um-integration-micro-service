package com.s3groupinc.gateway.api.controllers;

import com.google.gson.Gson;
import com.s3groupinc.gateway.api.entity.Permissions;
import com.s3groupinc.gateway.api.errors.PermissionAlreadyExists;
import com.s3groupinc.gateway.api.repo.PermissionsRepository;
import com.s3groupinc.gateway.api.services.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Manages CRUD (Create Read Update Delete) operations of um permissions.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PermissionsController {
    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private PermissionsRepository permissionsRepository;

    /**
     * Get all permissions.
     *
     * @return - on success, returns the list of all permissions.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/permissions")
    private List<String> getAllPermissions() {
        return permissionsService.getAllPermissions();
    }

    /**
     * Get a specific permission with matched permissionName.
     *
     * @param name - field of the permission
     * @return - on success, returns the found specific permission.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/permissions/{name}")
    public Permissions getPermissionByName(@PathVariable String name) throws Exception {
        //return permissionsService.getPermissionByName(name);
        List<Permissions> permissionsList = permissionsRepository.findOneByPermissionName(name);

        if (!permissionsList.isEmpty()) {
            return permissionsList.stream().findFirst().get();
        }
        return null;
    }

    /**
     * Add a permission
     *
     * @param permissions - jsonUser, which is a json class to add the permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/permissions")
    public ResponseEntity<String> addPermissions(@RequestBody Permissions permissions) throws PermissionAlreadyExists {
        permissionsService.addPermissions(permissions);
        Gson gson = new Gson();
        String message = "Permission with name " + permissions.getPermissionName() + " is successfully added.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Update a permission with matched permissionName
     *
     * @param name        - field of the permission
     * @param permissions - jsonUser, which is a json class to update the existing permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PutMapping("/permissions/{name}")
    public ResponseEntity<String> updatePermissionsByName(@PathVariable String name, @RequestBody @Valid Permissions permissions) throws Exception {
        permissionsService.updatePermissionsByName(name, permissions);
        Gson gson = new Gson();
        String message = "Permissions with name " + name + " is successfully updated.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Delete a permission with matched permissionName
     *
     * @param name - field of the permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @DeleteMapping("/permissions/{name}")
    public ResponseEntity<String> deletePermissionsByName(@PathVariable String name) throws Exception {
        permissionsService.deletePermissionsByName(name);
        Gson gson = new Gson();
        String message = "Permissions with name " + name + " is successfully deleted.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }
}