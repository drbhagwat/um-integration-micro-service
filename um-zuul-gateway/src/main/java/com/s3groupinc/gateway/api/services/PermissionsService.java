package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.entity.Permissions;
import com.s3groupinc.gateway.api.errors.PermissionAlreadyExists;
import com.s3groupinc.gateway.api.repo.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides CRUD services for Permissions Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Service
public class PermissionsService {
    @Autowired
    private PermissionsRepository permissionsRepository;

    /**
     * Get all permissions.
     *
     * @return - on success, returns the list of all permissions with distinct name.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<String> getAllPermissions() {
        List<Permissions> permissionsList = new ArrayList<Permissions>();
//        Integer usersId = null;
//        permissionsList = permissionsRepository.findByUsersId(usersId);
        permissionsList = permissionsRepository.findAll();
        List<String> permissionName = permissionsList.stream().map(permissions -> permissions.getPermissionName()).distinct().collect(Collectors.toList());
        return permissionName;
    }

    /**
     * Get a specific permission with id(primary key).
     *
     * @param id - primary key of the permission.
     * @return - on success, returns the found specific permission.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Permissions getPermissionWithId(int id) throws Exception {
        Optional<Permissions> permissions = permissionsRepository.findById(id);

        if (permissions.isEmpty()) {
            throw new Exception("Permissions id " + id + " is not found.");
        }
        return permissions.get();
    }

    /**
     * Get a specific permission with matched permissionName.
     *
     * @param name - field of the permission.
     * @return - on success, returns the list of found specific permission.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<Permissions> getPermission(String name) throws Exception {

        List<Permissions> permissions = permissionsRepository.findOneByPermissionName(name);

        if (permissions.isEmpty()) {
            throw new Exception("Permissions with name " + name + " is not found.");
        }
        return permissions;
    }

    /**
     * Add a permission
     *
     * @param permissions - jsonUser, which is a json class to add the permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Permissions addPermissions(Permissions permissions) throws PermissionAlreadyExists {

        Optional<Permissions> existingPermissions = permissionsRepository.findByPermissionName(permissions.getPermissionName());
        if (existingPermissions.isPresent()) {
            throw new PermissionAlreadyExists("Permission name with " + permissions.getPermissionName() + " is already exists.");
        }
        permissions.setPermissionName(permissions.getPermissionName().trim());
        return permissionsRepository.save(permissions);
    }

    /**
     * Update a permission with matched permissionName
     *
     * @param name   - field of the permissions
     * @param permissions - jsonUser, which is a json class to update the existing permissions
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void updatePermissionsByName(String name, Permissions permissions) throws Exception {
        List<Permissions> existingPermissions = getPermission(name);
        String permissionName = "";
        for (Permissions p : existingPermissions) {
            permissionName = permissions.getPermissionName();
            p.setPermissionName(permissionName.trim());
            permissionsRepository.save(p);
        }
    }

    /**
     * Delete a permission with id(primary key)
     *
     * @param id - primary key of the permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void deletePermissions(int id) throws Exception {
        Permissions permissions = getPermissionWithId(id);
        permissionsRepository.delete(permissions);
    }

    /**
     * Delete a permission with matched permissionName
     *
     * @param name - field of the permission
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void deletePermissionsByName(String name) throws Exception {
        List<Permissions> existingPermissions = getPermission(name);
        for (Permissions p : existingPermissions) {
            permissionsRepository.delete(p);
        }
    }
}