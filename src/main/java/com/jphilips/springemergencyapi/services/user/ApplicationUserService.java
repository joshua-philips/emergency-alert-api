package com.jphilips.springemergencyapi.services.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final OtpService otpService;

    /** User registration [non-staff] */
    public ApplicationUser registerUser(RegisterRequest request) {

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

        userRepository.save(user);

        SimpleMailMessage otpMessage = mailService.sendMessage(user.getUsername(), "One time password",
                otpService.createOtp(user));
        System.out.println(otpMessage);

        return user;
    }

    /** User login [non-staff] */
    public ApplicationUser loginUser(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));

        ApplicationUser user = userRepository
                .findByUsername(request.getUsername()).orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        user.setToken(jwtToken);

        return user;

    }

    public Iterable<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    public ApplicationUser updateUserRole(UserUpdateRequest request) throws Exception {
        Set<Role> roles = new HashSet<>();

        ApplicationUser user = userRepository.findById(request.getUser_id()).get();
        Role role = rolesService.getRoleById(request.getRole_id());

        if (user == null)
            throw new Exception("User does not exist");

        if (role == null)
            throw new Exception("Role not found");

        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    public String resetPassword(String username, String baseUrl) {

        ApplicationUser user = userRepository.findByUsername(username).get();

        String token = createPasswordResetToken(user);
        SimpleMailMessage message = mailService
                .constructResetTokenEmail(baseUrl, token, user.getUsername(), true);
        System.out.println(message);

        return "Password reset request successful. Mail sent to email " + username;

    }

    public ApplicationUser changePasswordWithToken(String token, String password) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        PasswordResetToken passwordResetToken = resetTokenRepository.findByToken(token).get();
        ApplicationUser user = passwordResetToken.getUser();

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Invalid token");
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetToken.setExpiryDate(LocalDateTime.now());
        resetTokenRepository.delete(passwordResetToken);
        return user;
    }

    private String createPasswordResetToken(ApplicationUser user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(resetToken);
        return token;
    }

    public ApplicationUser verifyOtp(String username, String code) throws Exception {
        ApplicationUser user = otpService.verifyApplicationOtp(code, username);

        String token = jwtService.generateToken(user);
        user.setToken(token);
        return user;
    }

    public String resendOtp(String username) {
        ApplicationUser user = userRepository.findByUsername(username).get();
        SimpleMailMessage otpMessage = mailService.sendMessage(user.getUsername(), "One time password",
                otpService.createOtp(user));
        System.out.println(otpMessage);

        return "One Time Password sent to: " + user.getUsername();

    }

}
