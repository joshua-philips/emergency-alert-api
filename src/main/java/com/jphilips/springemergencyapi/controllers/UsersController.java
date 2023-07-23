package com.jphilips.springemergencyapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.users.UserResponse;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.dto.users.UsersResponse;
import com.jphilips.springemergencyapi.services.ApplicationUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class UsersController {

    private final ApplicationUserService userService;

    @GetMapping("/")
    public UsersResponse getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/update-role")
    public UserResponse postMethodName(@RequestBody UserUpdateRequest body) {
        return userService.updateUserRole(body);
    }

}
