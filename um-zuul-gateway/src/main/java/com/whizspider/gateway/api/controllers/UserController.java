package com.whizspider.gateway.api.controllers;

import com.whizspider.gateway.api.errors.UserAlreadyExists;
import com.whizspider.gateway.api.errors.UserNotFound;
import com.whizspider.gateway.api.model.User;
import com.whizspider.gateway.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping()
  public Page<User> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                           @RequestParam(defaultValue = "1") Integer pageSize,
                           @RequestParam(defaultValue =
                               "lastUpdatedDateTime") String sortBy,
                           @RequestParam(defaultValue = "D") String orderBy,
                           Model model) {
    return userService.getAll(pageNo, pageSize, sortBy, orderBy);
  }

  @GetMapping("/{name}")
  public User get(@PathVariable String name) throws UserNotFound {
    return userService.get(name);
  }

  @PostMapping("/add")
  public User add(@Valid User user) throws UserAlreadyExists {
    return userService.add(user);
  }

  @PutMapping("/update/{name}")
  public User update(@PathVariable String name, @Valid User user) throws UserNotFound {
    return userService.update(name, user);
  }
  @GetMapping("/delete/{name}")
  public void delete(@PathVariable String name) throws UserNotFound {
    userService.delete(name);
  }
}
