package com.whizspider.gateway.api.controllers;

import com.whizspider.gateway.api.errors.RoleAlreadyExists;
import com.whizspider.gateway.api.errors.RoleNotFound;
import com.whizspider.gateway.api.model.Role;
import com.whizspider.gateway.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/roles")
public class RoleController {
  @Autowired
  private RoleService roleService;

  @GetMapping()
  public Page<Role> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                           @RequestParam(defaultValue = "1") Integer pageSize,
                           @RequestParam(defaultValue =
                               "lastUpdatedDateTime") String sortBy,
                           @RequestParam(defaultValue = "D") String orderBy,
                           Model model) {
    return roleService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  @PostMapping("/add")
  public Role add(@Valid Role Role) throws RoleAlreadyExists {
    return roleService.add(Role);
  }

  @GetMapping("/delete/{name}")
  public void delete(@PathVariable String name) throws RoleNotFound {
    roleService.delete(name);
  }
}
