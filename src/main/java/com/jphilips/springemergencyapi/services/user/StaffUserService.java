package com.jphilips.springemergencyapi.services.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.repositories.PasswordResetTokenRepository;
import com.jphilips.springemergencyapi.repositories.StaffUserRepository;
import com.jphilips.springemergencyapi.services.JwtService;
import com.jphilips.springemergencyapi.services.MailService;
import com.jphilips.springemergencyapi.services.RolesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffUserService {

    private final StaffUserRepository userRepository;
    private final RolesService rolesService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final MailService mailService;

    

}   
