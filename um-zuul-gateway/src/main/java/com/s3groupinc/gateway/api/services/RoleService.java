package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.entity.Role;
import com.s3groupinc.gateway.api.errors.RoleAlreadyExists;
import com.s3groupinc.gateway.api.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides CRUD services for Role Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all roles.
     *
     * @return - on success, returns the list of all roles with distinct name.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<String> getAllRoles() {
        List<Role> roleList = new ArrayList<Role>();
//        Integer usersId = null;
//        roleList = roleRepository.findByUsersId(usersId);
        roleList = roleRepository.findAll();
        List<String> roleName = roleList.stream().map(role -> role.getName()).distinct().collect(Collectors.toList());
        return roleName;
    }

    /**
     * Get a specific role with id(primary key).
     *
     * @param id - primary key of the role.
     * @return - on success, returns the found specific role.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Role getRoleWithId(int id) throws Exception {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isEmpty()) {
            throw new Exception("Role id " + id + " is not found.");
        }
        return role.get();
    }

    /**
     * Get a specific role with matched roleName.
     *
     * @param name - field of the role.
     * @return - on success, returns the list of found specific role.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<Role> getRole(String name) throws Exception {

        List<Role> role = roleRepository.findOneByName(name);

        if (role.isEmpty()) {
            throw new Exception("Role with name " + name + " is not found.");
        }
        return role;
    }

    /**
     * Add a role
     *
     * @param role - jsonUser, which is a json class to add the role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Role addRole(Role role) throws RoleAlreadyExists {
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            throw new RoleAlreadyExists("Role name with " + role.getName() + " is already exists.");
        } else {
            role.setName(role.getName().trim());
            roleRepository.save(role);
        }
        return role;
    }

    /**
     * Update a role with matched roleName
     *
     * @param name   - field of the role
     * @param role - jsonUser, which is a json class to update the existing role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void updateRoleByName(String name, Role role) throws Exception {
        List<Role> existingRoles = getRole(name);
        String roleName = "";
        for (Role r : existingRoles) {
            roleName = role.getName();
            r.setName(roleName.trim());
            roleRepository.save(r);
        }
    }

    /**
     * Delete a role with id(primary key)
     *
     * @param id - primary key of the role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void deleteRole(int id) throws Exception {
        Role role = getRoleWithId(id);
        roleRepository.delete(role);
    }

    /**
     * Delete a role with matched roleName
     *
     * @param name - field of the role
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public void deleteRoleByName(String name) throws Exception {
        List<Role> existingRoles = getRole(name);
        for (Role r : existingRoles) {
            roleRepository.delete(r);
        }
    }
}