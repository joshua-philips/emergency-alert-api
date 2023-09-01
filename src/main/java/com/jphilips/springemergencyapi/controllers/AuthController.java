package com.jphilips.springemergencyapi.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.auth.LoginRequest;
import com.jphilips.springemergencyapi.dto.auth.LoginResponse;
import com.jphilips.springemergencyapi.dto.auth.RegisterRequest;
import com.jphilips.springemergencyapi.dto.auth.RegisterResponse;
import com.jphilips.springemergencyapi.dto.users.UserResponse;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.services.ApplicationUserService;
import com.jphilips.springemergencyapi.services.user.ApplicationUserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final ApplicationUserService authService;
    private final ApplicationUserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest body) {
        return authService.registerUser(body);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest body) {
        return authService.loginUser(body);
    }

    @PostMapping("/forgot-password")
    public DefaultResponse forgotPass(@RequestBody UserUpdateRequest request,
            HttpServletRequest servletRequest) {
        return userService.resetPassword(request.getUsername(), "http://" + servletRequest.getServerName() + ":"
                + servletRequest.getServerPort() + servletRequest.getContextPath());
    }

    @PostMapping("/change-password/{token}")
    public UserResponse changePassWithToken(@PathVariable String token, @RequestBody UserUpdateRequest request) {
        if (request.getPassword() == null || request.getConfirm_password() == null
                || !request.getConfirm_password().equals(request.getPassword())) {
            return new UserResponse("Passwords do not match", null);
        }
        return userService.changePasswordWithToken(token, request.getPassword());
    }

}
