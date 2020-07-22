package com.whizspider.gateway.api.controllers;

import com.whizspider.gateway.api.json.LoginInput;
import com.whizspider.gateway.api.json.LoginOutput;
import com.whizspider.gateway.api.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
  @Autowired
  LoginService loginService;

  @PostMapping("/login")
  public LoginOutput login(@RequestBody @Valid LoginInput loginInput) {
    return loginService.login(loginInput);
  }
}
