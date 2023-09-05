package com.jphilips.springemergencyapi.controllers.application;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.auth.LoginRequest;
import com.jphilips.springemergencyapi.dto.auth.RegisterRequest;
import com.jphilips.springemergencyapi.dto.auth.TokenRequest;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.models.user.ApplicationUser;
import com.jphilips.springemergencyapi.services.user.ApplicationUserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {

    private final ApplicationUserService userService;

    @PostMapping("/register")
    public DefaultResponse register(@RequestBody RegisterRequest body) {
        ApplicationUser user = userService.registerUser(body);
        return new DefaultResponse("Successfuly registered. Verification sent to "
                + user.getUsername(), user);
    }

    @PostMapping("/otp-verification")
    public DefaultResponse otpVerification(@RequestBody TokenRequest body) throws Exception {
        return new DefaultResponse("One time password verified",
                userService.verifyOtp(body.getUsername(), body.getOtp()));
    }

    @PostMapping("/otp-resend")
    public DefaultResponse otpResend(@RequestBody TokenRequest body) throws Exception {
        return new DefaultResponse(userService.resendOtp(body.getUsername()), null);
    }

    @PostMapping("/login")
    public DefaultResponse login(@RequestBody LoginRequest body) {
        return new DefaultResponse("Login successful", userService.loginUser(body));
    }

    @PostMapping("/forgot-password")
    public DefaultResponse forgotPass(@RequestBody UserUpdateRequest request,
            HttpServletRequest servletRequest) {
        return new DefaultResponse("Password reset email sent",
                userService.resetPassword(request.getUsername(), "http://" + servletRequest.getServerName() + ":"
                        + servletRequest.getServerPort() + servletRequest.getContextPath()));
    }

    @PostMapping("/change-password/{token}")
    public DefaultResponse changePassWithToken(@PathVariable String token, @RequestBody UserUpdateRequest request)
            throws Exception {
        if (request.getPassword() == null || request.getConfirm_password() == null
                || !request.getConfirm_password().equals(request.getPassword())) {
            throw new Exception("Passwords do not match");
        }
        return new DefaultResponse("Password changed successfuly",
                userService.changePasswordWithToken(token, request.getPassword()));
    }

}
