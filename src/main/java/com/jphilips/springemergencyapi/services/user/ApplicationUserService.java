package com.jphilips.springemergencyapi.services.user;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.auth.LoginRequest;
import com.jphilips.springemergencyapi.dto.auth.RegisterRequest;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.models.PasswordResetToken;
import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.models.user.ApplicationUser;
import com.jphilips.springemergencyapi.repositories.ApplicationUserRepository;
import com.jphilips.springemergencyapi.repositories.PasswordResetTokenRepository;
import com.jphilips.springemergencyapi.services.JwtService;
import com.jphilips.springemergencyapi.services.MailService;
import com.jphilips.springemergencyapi.services.RolesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository userRepository;
    private final RolesService rolesService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final MailService mailService;

    /** User registration [non-staff] */
    public DefaultResponse registerUser(RegisterRequest request) {
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
                return DefaultResponse.builder()
                        .message("Username already in use").build();
            }

            userRepository.save(user);
            String token = jwtService.generateToken(user);
            user.setToken(token);

            return DefaultResponse.builder()
                    .message("User created successfully")
                    .data(user)
                    .build();
        } catch (Exception e) {
            return DefaultResponse.builder().message(e.getMessage()).build();
        }
    }

    /** User login [non-staff] */
    public DefaultResponse loginUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()));

            ApplicationUser user = userRepository
                    .findByUsername(request.getUsername()).orElseThrow();

            String jwtToken = jwtService.generateToken(user);
            user.setToken(jwtToken);
            return DefaultResponse
                    .builder()
                    .message("Login successful").data(user).build();
        } catch (Exception e) {
            return DefaultResponse.builder().message(e.getMessage()).build();
        }
    }

    public DefaultResponse getAllUsers() {
        return new DefaultResponse("All users loaded", userRepository.findAll());
    }

    public DefaultResponse updateUserRole(UserUpdateRequest request) {
        Set<Role> roles = new HashSet<>();

        try {
            ApplicationUser user = userRepository.findById(request.getUser_id()).get();
            Role role = rolesService.getRoleById(request.getRole_id());

            if (user == null)
                throw new Exception("User does not exist");

            if (role == null)
                throw new Exception("Role not found");

            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);

            return new DefaultResponse("User role updated successfully", user);
        } catch (Exception e) {
            return new DefaultResponse(e.getMessage(), null);
        }
    }

    public DefaultResponse resetPassword(String username, String baseUrl) {
        try {
            ApplicationUser user = userRepository.findByUsername(username).get();

            String token = createPasswordResetToken(username, user);
            SimpleMailMessage message = mailService.constructResetTokenEmail(baseUrl, token, user);
            System.out.printf("%s\n%s\n%s\n%s\n",
                    message.getSubject(), Arrays.toString(message.getTo()), message.getFrom(),
                    message.getText());

            return new DefaultResponse("Password reset request successful. Mail sent to email", username);
        } catch (NoSuchElementException e) {
            return new DefaultResponse("User does not exist", null);
        } catch (Exception e) {
            return new DefaultResponse(e.getMessage().length() > 30
                    ? "Request failed"
                    : e.getMessage(), null);
        }

    }

    private String createPasswordResetToken(String username, ApplicationUser user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(resetToken);
        return token;

    }

    public DefaultResponse changePasswordWithToken(String token, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            PasswordResetToken passwordResetToken = resetTokenRepository.findByToken(token).get();
            ApplicationUser user = passwordResetToken.getUser();

            if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new Exception("Invalid token");
            }

            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            passwordResetToken.setExpiryDate(LocalDateTime.now());
            resetTokenRepository.delete(passwordResetToken);

            return new DefaultResponse("Password changed", user);

        } catch (Exception e) {
            return new DefaultResponse(e.getMessage().length() > 30
                    ? "Request failed"
                    : e.getMessage(), null);
        }
    }

    // TODO: One time passwords
}
