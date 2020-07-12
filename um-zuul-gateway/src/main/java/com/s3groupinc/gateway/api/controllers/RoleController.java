package com.s3groupinc.gateway.api.controllers;

import com.google.gson.Gson;
import com.s3groupinc.gateway.api.entity.Role;
import com.s3groupinc.gateway.api.errors.RoleAlreadyExists;
import com.s3groupinc.gateway.api.repo.RoleRepository;
import com.s3groupinc.gateway.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Manages CRUD (Create Read Update Delete) operations of um role.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all roles.
     *
     * @return - on success, returns the list of all roles.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/roles")
    private List<String> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * Get a specific role with matched roleName.
     *
     * @param name - field of the role
     * @return - on success, returns the found specific role.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/roles/{name}")
    public Role getRoleByName(@PathVariable String name) throws Exception {
        List<Role> roleList = roleRepository.findOneByName(name);

        if (!roleList.isEmpty()) {
//            groupsList = Collections.singletonList(groupsList.stream().findFirst().get());
            return roleList.stream().findFirst().get();
        }
        return null;
        //return roleService.getRoleByName(name);
    }

    /**
     * Add a role
     *
     * @param role - jsonUser, which is a json class to add the role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/roles")
    public ResponseEntity<String> addRole(@RequestBody Role role) throws RoleAlreadyExists {
        roleService.addRole(role);
        Gson gson = new Gson();
        String message = "Role with name " + role.getName() + " is successfully added.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Update a role with matched roleName
     *
     * @param name  - field of the role
     * @param role - jsonUser, which is a json class to update the existing role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PutMapping("/roles/{name}")
    public ResponseEntity<String> updateRoleByName(@PathVariable String name, @RequestBody @Valid Role role) throws Exception {
        roleService.updateRoleByName(name, role);
        Gson gson = new Gson();
        String message = "Role with name " + name + " is successfully updated.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Delete a role with matched roleName
     *
     * @param name - field of the role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @DeleteMapping("/roles/{name}")
    public ResponseEntity<String> deleteRole(@PathVariable String name) throws Exception {
        roleService.deleteRoleByName(name);
        Gson gson = new Gson();
        String message = "Role with name " + name + " is successfully deleted.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }
}