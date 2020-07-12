package com.s3groupinc.gateway.api.controllers;

import com.google.gson.Gson;
import com.s3groupinc.gateway.api.entity.Groups;
import com.s3groupinc.gateway.api.errors.GroupNotFound;
import com.s3groupinc.gateway.api.errors.GroupsAlreadyExists;
import com.s3groupinc.gateway.api.repo.GroupsRepository;
import com.s3groupinc.gateway.api.services.GroupsService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manages CRUD (Create Read Update Delete) operations of um groups.
 *
 * @author : Anghulakshmi B
 * @version : 1.0
 * @since : 2020-02-10
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GroupsController {
    @Autowired
    private GroupsService groupsService;

    @Autowired
    private GroupsRepository groupsRepository;

    /**
     * Get all groups.
     *
     * @return - on success, returns the list of all groups.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/groups")
    private List<String> getAllGroups() {
        return groupsService.getAllGroups();
    }

    /**
     * Get a specific group with matched groupName.
     *
     * @param name - field of the group
     * @return - on success, returns the found specific group.
     */
//    @GetMapping("/groups/{name}")
//    public Groups getGroupByName(@PathVariable String name) {
//        return groupsRepository.findOneByGroupName(name).orElseThrow(() -> new GroupNotFound("Group with name "
//                + name + " is not found."));
//    }

    @GetMapping("/groups/{name}")
    public Groups getGroupByName(@PathVariable String name) throws Exception {
        List<Groups> groupsList = groupsRepository.findByGroupName(name);

        if (!groupsList.isEmpty()) {
//            groupsList = Collections.singletonList(groupsList.stream().findFirst().get());
            return groupsList.stream().findFirst().get();
        }
        return null;
    }

    /**
     * Add a group
     *
     * @param groups - jsonUser, which is a json class to add the group
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/groups")
    public ResponseEntity<String> addGroups(@RequestBody Groups groups) throws GroupsAlreadyExists {
        groupsService.addGroups(groups);
        Gson gson = new Gson();
        String message = "Group with name " + groups.getGroupName() + " is successfully added.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Update a group with matched groupName
     *
     * @param name   - field of the group
     * @param groups - jsonUser, which is a json class to update the existing group
     * @return -   on success, returns the success message.
     */
    @PutMapping("/groups/{name}")
    public ResponseEntity<String> updateGroupsByName(@RequestBody Groups groups, @PathVariable String name) throws Exception {
        groupsService.updateGroupsByName(groups, name);
        Gson gson = new Gson();
        String message = "Groups with name " + name + " is successfully updated.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    /**
     * Delete a group with matched groupName
     *
     * @param name - field of the group
     * @return -   on success, returns the success message.
     */
    @DeleteMapping("/groups/{name}")
    public ResponseEntity<String> deleteGroupsByName(@PathVariable String name) throws Exception {
        groupsService.deleteGroupsByName(name);
        Gson gson = new Gson();
        String message = "Groups with name " + name + " is successfully deleted.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    @PostMapping("/groupswithpermissions")
    public ResponseEntity<String> addPermissionsToGroup(@RequestBody @Valid Groups groups) {
        try {
            groupsService.addPermissionsToGroup(groups);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Gson gson = new Gson();
        String message = "Permissions added to " + groups.getGroupName() + " successfully.";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    @PostMapping("/addmenustogroup")
    public ResponseEntity<String> addMenusToGroup(@RequestBody @Valid Groups groups) {
        try {
            groupsService.addMenusToGroup(groups);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Gson gson = new Gson();
        String message = "Menus added to " + groups.getGroupName() + " successfully. ";
        String json = gson.toJson(message);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/getgroup/{groupname}")
    public Groups getByGroupName(@PathVariable String groupname) {
        return groupsService.getByGroupName(groupname);
    }
}