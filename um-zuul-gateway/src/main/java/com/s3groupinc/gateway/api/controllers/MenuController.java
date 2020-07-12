package com.s3groupinc.gateway.api.controllers;

import com.s3groupinc.gateway.api.entity.Menu;
import com.s3groupinc.gateway.api.services.MenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Manages CRUD (Create Read Update Delete) operations of menu.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2020-03-05
 */

@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * Add a menu
     *
     * @param menu - jsonUser, which is a json class to add the menu.
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    @PostMapping("/menus")
    public ResponseEntity<Menu> addMenu(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.addMenu(menu));
    }

    /**
     * Get all menus.
     *
     * @return - on success, returns the list of all menus.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    /**
     * Get all menus.
     *
     * @return - on success, returns the menu with distinct.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    @GetMapping("/getmenu")
    public List<String> getMenu() {
        return menuService.getMenu();
    }

    @GetMapping("/getallparentmenus")
    public List<String> getAllParentMenus() { return menuService.getAllParentMenus(); }

    @GetMapping("/getallparentchildmenus")
    public List<Menu> getAllParentAndChildMenus() { return menuService.findAllParentWithChildMenus(); }
}