package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.entity.Menu;
import com.s3groupinc.gateway.api.entity.Permissions;
import com.s3groupinc.gateway.api.errors.*;
import com.s3groupinc.gateway.api.repo.GroupsRepository;
import com.s3groupinc.gateway.api.repo.MenuRepository;
import com.s3groupinc.gateway.api.repo.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides CRUD services for Groups Controller.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@Service
public class GroupsService {
    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private MenuRepository menuRepository;

    /**
     * Get all groups.
     *
     * @return - on success, returns the list of all groups with distinct name.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<String> getAllGroups() {
        return groupsRepository.findAll().stream().map(Groups::getGroupName).distinct().collect(Collectors.toList());
    }

    /**
     * Get a specific group with id(primary key).
     *
     * @param id - primary key of the group
     * @return - on success, returns the found specific group.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Groups getGroupWithId(int id) {
        Optional<Groups> groups = groupsRepository.findById(id);

        if (groups.isEmpty()) {
            throw new GroupNotFound("Group id " + id + " is not found.");
        }
        return groups.get();
    }

    /**
     * Get a specific group with matched groupName.
     *
     * @param name - field of the group
     * @return - on success, returns the list of found specific group.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<Groups> getGroupByName(String name) {
        List<Groups> groups = groupsRepository.findByGroupName(name);

        if (groups.isEmpty()) {
            throw new GroupNotFound("Group with name " + name + " is not found.");
        }
        return groups;
    }

    /**
     * Add a group
     *
     * @param groups - jsonUser, which is a json class to add the group
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Groups addGroups(Groups groups) throws GroupsAlreadyExists {
        Optional<Groups> existingGroups = groupsRepository.findOneByGroupName(groups.getGroupName());
        if (existingGroups.isPresent()) {
            throw new GroupsAlreadyExists("Group name with " + groups.getGroupName() + " is already exists.");
        }
        groups.setGroupName(groups.getGroupName().trim());
        return groupsRepository.save(groups);
    }

    /**
     * Update a group with matched groupName
     *
     * @param name   - field of the group
     * @param groups - jsonUser, which is a json class to update the existing group
     * @return -   on success, returns the success message.
     */
    public void updateGroupsByName(Groups groups, String name) {
        List<Groups> existingGroups = getGroupByName(name);
        String grpName = "";
        for (Groups g : existingGroups) {
            grpName = groups.getGroupName();
            g.setGroupName(grpName.trim());
            groupsRepository.save(g);
        }
    }

    /**
     * Delete a group with id(primary key)
     *
     * @param id - primary key of the group
     * @return -   on success, returns the success message.
     */
    public void deleteGroups(int id) {
        Groups groups = getGroupWithId(id);
        groupsRepository.delete(groups);
    }

    /**
     * Delete a group with matched groupName
     *
     * @param name - field of the group
     * @return -   on success, returns the success message.
     */
    public void deleteGroupsByName(String name) {
        List<Groups> existingGroups = getGroupByName(name);
        for (Groups g : existingGroups) {
            groupsRepository.delete(g);
        }
    }

    public void addPermissionsToGroup(Groups groups) throws PermissionAlreadyExists {
        String groupName = groups.getGroupName();
        Optional<Groups> existingGroups = groupsRepository.findGroups(groupName);
        Groups group = existingGroups.orElseThrow(() -> new GroupNotFound("Group with " + groupName + " does not exists."));
        List<Permissions> newPermissionList = new ArrayList<>();

        if (!groups.getPermissionsList().isEmpty()) {
            for (Permissions permission : groups.getPermissionsList()) {
                if (group.getPermissionsList().stream().anyMatch(p -> p.getPermissionName().equalsIgnoreCase(permission.getPermissionName()))) {
                    throw new PermissionAlreadyExists("Permission name with " + permission.getPermissionName() + " is already found for the group " + group.getGroupName());
                }
                List<Permissions> permissions = permissionsRepository.findOneByPermissionName(permission.getPermissionName());
//                if (permissions.isEmpty()) {
//                    throw new PermissionNotFound("Permission name with " + permission.getPermissionName() + " is not found.");
//                }
                permission.setId(null);
                newPermissionList.add(permission);
            }
            newPermissionList.addAll(group.getPermissionsList());
            group.setPermissionsList(newPermissionList.stream().filter(distinctByKey(Permissions::getPermissionValue)).collect(Collectors.toList()));
        }
        groupsRepository.save(group);
    }

    public void addMenusToGroup(Groups groups) {
        String groupName = groups.getGroupName();
        Optional<Groups> existingGroups = groupsRepository.findGroups(groupName);
        Groups group = existingGroups.orElseThrow(() -> new GroupNotFound("Group with " + groupName + " does not exists."));
        List<Menu> newMenuList = new ArrayList<>();

        if (!groups.getMenuList().isEmpty()) {
            for (Menu menu : groups.getMenuList()) {
                if (group.getMenuList().stream().anyMatch(m -> m.getName().equalsIgnoreCase(menu.getName()))) {
                    throw new MenuAlreadyExists("Menu name with " + menu.getName() + " is already found for the group " + group.getGroupName());
                }
                List<Menu> menus = menuRepository.findOneByName(menu.getName());
//                `if (menus.isEmpty()) {
//                    throw new MenuNotFound("Menu name with " + menu.getName() + " is not found.");
//                }`
                menu.setId(null);
                newMenuList.add(menu);
            }
            newMenuList.addAll(group.getMenuList());
            group.setMenuList(newMenuList.stream().filter(distinctByKey(Menu::getName)).collect(Collectors.toList()));
        }
        groupsRepository.save(group);
    }

    public Groups getByGroupName(String groupName) {
        Optional<Groups> groups = groupsRepository.findGroups(groupName);
        return groups.orElseThrow(() -> new GroupNotFound("Group does not exists."));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}