package com.s3groupinc.gateway.api.services;

import com.s3groupinc.gateway.api.entity.Menu;
import com.s3groupinc.gateway.api.repo.MenuRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides CRUD services of menu controller.
 *
 * @author : Sachin Kulkarni
 * @version : 1.0
 * @since : 2020-03-05
 */

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    /**
     * Add a menu
     *
     * @param menu - jsonUser, which is a json class to add the menu.
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */
    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    /**
     * Get all menus.
     *
     * @return - on success, returns the list of all menus.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /**
     * Get all menus.
     *
     * @return - on success, returns the menu with distinct.
     * @throws Exception - on failure, a global exception handler is called
     *                   which displays an appropriate error message.
     */
    public List<String> getMenu() {
        return getAllMenus().stream().map(Menu::getName).distinct().collect(Collectors.toList());
    }

    /**
     * Delete a menu with matched menuName
     *
     * @param name - field of the menu
     * @return -   on success, returns the success message.
     * @throws Exception - on failure, throws an appropriate exception.
     */

    public List<String> getAllParentMenus() {
        List<Menu> menuList =  menuRepository.findAllParentWithChildMenus();
        return menuList.stream().map(Menu::getName).distinct().collect(Collectors.toList());
    }

    public List<Menu> findAllParentWithChildMenus() {
        return menuRepository.findAllParentWithChildMenus();
    }

    public void deleteMenusWithId(int id) throws Exception {
        Menu menu = getMenuWithId(id);
        menuRepository.delete(menu);
    }

    public Menu getMenuWithId(int id) throws Exception {
        Optional<Menu> menu = menuRepository.findById(id);

        if (menu.isEmpty()) {
            throw new Exception("Menu id " + id + " is not found.");
        }
        return menu.get();
    }
}