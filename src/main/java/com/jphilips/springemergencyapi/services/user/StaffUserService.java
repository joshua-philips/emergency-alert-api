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
import com.jphilips.springemergencyapi.dto.auth.StaffRegisterRequest;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.models.PasswordResetToken;
import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.models.user.StaffUser;
import com.jphilips.springemergencyapi.repositories.PasswordResetTokenRepository;
import com.jphilips.springemergencyapi.repositories.StaffUserRepository;
import com.jphilips.springemergencyapi.services.JwtService;
import com.jphilips.springemergencyapi.services.MailService;
import com.jphilips.springemergencyapi.services.RolesService;
import com.jphilips.springemergencyapi.utils.RandomPasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffUserService {

    private final StaffUserRepository staffRepository;
    private final RolesService rolesService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final MailService mailService;
    private final OtpService otpService;

    public StaffUser createUser(StaffRegisterRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(rolesService.getRoleById(request.getRole_id()));

        String password = RandomPasswordGenerator.generateRandomPassword(8);

        StaffUser user = StaffUser.builder()
                .roles(roles)
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .username(request.getUsername())
                .password(passwordEncoder.encode(password))
                .is_account_locked(false)
                .is_account_enabled(true)
                .phone_number(request.getPhone())
                .build();

        staffRepository.save(user);
        System.out.println(mailService.registeredStaffEmail(password, user.getUsername()));
        return user;
    }

    public StaffUser login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));

        StaffUser user = staffRepository
                .findByUsername(request.getUsername()).orElseThrow();
        SimpleMailMessage otpMessage = mailService.sendMessage(user.getUsername(), "One time password",
                otpService.createOtp(user));
        System.out.println(otpMessage);

        return user;
    }

    public Iterable<StaffUser> getAllStaff() {
        return staffRepository.findAll();
    }

    public StaffUser updateStaffRole(UserUpdateRequest request) throws Exception {
        Set<Role> roles = new HashSet<>();

        StaffUser user = staffRepository.findById(request.getUser_id()).get();
        Role role = rolesService.getRoleById(request.getRole_id());

        if (user == null)
            throw new Exception("User does not exist");

        if (role == null)
            throw new Exception("Role not found");

        roles.add(role);
        user.setRoles(roles);
        staffRepository.save(user);

        return user;

    }

    public String resetPassword(String username, String baseUrl) {
        StaffUser user = staffRepository.findByUsername(username).get();

        String token = createPasswordResetToken(username, user);
        SimpleMailMessage message = mailService.constructResetTokenEmail(baseUrl, token, user.getUsername(), true);
        System.out.println(message);

        return "Password reset request successful. Mail sent to email " + username;

    }

    private String createPasswordResetToken(String username, StaffUser user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(resetToken);
        return token;

    }

    public StaffUser changePasswordWithToken(String token, String password) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        PasswordResetToken passwordResetToken = resetTokenRepository.findByToken(token).get();
        StaffUser user = passwordResetToken.getStaffUser();

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Invalid token");
        }

        user.setPassword(passwordEncoder.encode(password));
        staffRepository.save(user);

        passwordResetToken.setExpiryDate(LocalDateTime.now());
        resetTokenRepository.delete(passwordResetToken);

        return user;

    }

    public StaffUser verifyOtp(String username, String code) throws Exception {
        StaffUser user = otpService.verifyStaffOtp(code, username);

        String token = jwtService.generateToken(user);
        user.setToken(token);
        return user;
    }

    public String resendOtp(String username) {
        StaffUser user = staffRepository.findByUsername(username).get();
        SimpleMailMessage otpMessage = mailService.sendMessage(user.getUsername(), "One time password",
                otpService.createOtp(user));
        System.out.println(otpMessage);

        return "One Time Password sent to: " + user.getUsername();
    }
}
