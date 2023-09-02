package com.jphilips.springemergencyapi.controllers.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.auth.LoginRequest;
import com.jphilips.springemergencyapi.dto.auth.StaffRegisterRequest;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.services.RolesService;
import com.jphilips.springemergencyapi.services.user.ApplicationUserService;
import com.jphilips.springemergencyapi.services.user.StaffUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {

    private final ApplicationUserService userService;
    private final StaffUserService staffService;
    private final RolesService rolesService;

    @PostMapping("/login")
    public DefaultResponse staffLogin(@RequestBody LoginRequest body) {
        return new DefaultResponse("Login successful", staffService.login(body));
    }

    @PostMapping("/forgot-password")
    public DefaultResponse staffForgotPass(@RequestBody UserUpdateRequest request,
            HttpServletRequest servletRequest) {
        return new DefaultResponse("Password reset email sent",
                staffService.resetPassword(request.getUsername(), "http://" + servletRequest.getServerName() + ":"
                        + servletRequest.getServerPort() + servletRequest.getContextPath()));
    }

    @PostMapping("/change-password/{token}")
    public DefaultResponse changePassWithTokenStaff(@PathVariable String token, @RequestBody UserUpdateRequest request)
            throws Exception {
        if (request.getPassword() == null || request.getConfirm_password() == null
                || !request.getConfirm_password().equals(request.getPassword())) {
            throw new Exception("Passwords do not match");
        }
        return new DefaultResponse("Password changed successfuly",
                staffService.changePasswordWithToken(token, request.getPassword()));
    }

    @GetMapping("/list-users")
    @SecurityRequirement(name = "Authorization")
    public DefaultResponse getUsers() {
        return new DefaultResponse("Users loaded", userService.getAllUsers());
    }

    @PostMapping("/update-staff-role")
    @SecurityRequirement(name = "Authorization")
    public DefaultResponse postMethodName(@RequestBody UserUpdateRequest body) throws Exception {
        return new DefaultResponse("Role updated", staffService.updateStaffRole(body));
    }

    @GetMapping("/list-roles")
    @SecurityRequirement(name = "Authorization")
    public DefaultResponse listRoles() {
        return new DefaultResponse("Roles loaded", rolesService.listAllRoles());
    }

    @PostMapping("/create-staff")
    @SecurityRequirement(name = "Authorization")
    public DefaultResponse register(@RequestBody StaffRegisterRequest body) {
        return new DefaultResponse("Successfuly registered", staffService.createUser(body));
    }

    @GetMapping("/list-staff")
    @SecurityRequirement(name = "Authorization")
    public DefaultResponse listStaff() {
        return new DefaultResponse("Users loaded", staffService.getAllStaff());
    }

}
