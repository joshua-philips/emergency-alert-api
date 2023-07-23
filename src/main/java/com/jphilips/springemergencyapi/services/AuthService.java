package com.jphilips.springemergencyapi.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.auth.LoginRequest;
import com.jphilips.springemergencyapi.dto.auth.LoginResponse;
import com.jphilips.springemergencyapi.dto.auth.RegisterRequest;
import com.jphilips.springemergencyapi.dto.auth.RegisterResponse;
import com.jphilips.springemergencyapi.models.ApplicationUser;
import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.repositories.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationUserRepository userRepository;
    private final RolesService rolesService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse registerUser(RegisterRequest request) {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(rolesService.getRoleById(2L));

            ApplicationUser user = ApplicationUser.builder()
                    .roles(roles)
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .is_account_locked(false)
                    .is_account_enabled(true)
                    .phone_number(request.getPhone())
                    .build();

            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return RegisterResponse.builder()
                        .message("Username already in use").build();
            }

            userRepository.save(user);
            String token = jwtService.generateToken(user);

            return RegisterResponse.builder()
                    .token(token)
                    .message("User created successfuly")
                    .user(user)
                    .build();
        } catch (Exception e) {
            return RegisterResponse.builder().message(e.getMessage()).build();
        }
    }

    public LoginResponse loginUser(LoginRequest request) {
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(),
                                    request.getPassword()));

            ApplicationUser user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            String jwtToken = jwtService.generateToken(user);
            return LoginResponse.builder().message("Login successful").user(user).token(jwtToken).build();
        } catch (Exception e) {
            return LoginResponse.builder().message(e.getMessage()).build();
        }
    }

}
