package com.gustavo.ToDoList.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

   private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/authenticate")
    public String authenticate(Authentication authentication){
        return service.authenticate(authentication);
    }
}
