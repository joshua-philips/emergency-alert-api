package com.jphilips.springemergencyapi.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.users.UserResponse;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.dto.users.UsersResponse;
import com.jphilips.springemergencyapi.models.ApplicationUser;
import com.jphilips.springemergencyapi.models.PasswordResetToken;
import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.repositories.ApplicationUserRepository;
import com.jphilips.springemergencyapi.repositories.PasswordResetTokenRepository;
import com.jphilips.springemergencyapi.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    public UsersResponse getAllUsers() {
        return new UsersResponse("All users loaded", userRepository.findAll());
    }

    public UserResponse updateUserRole(UserUpdateRequest request) {
        Set<Role> roles = new HashSet<>();

        try {
            ApplicationUser user = userRepository.findById(request.getUser_id()).get();
            Role role = roleRepository.findById(request.getRole_id()).get();

            if (user == null)
                throw new Exception("User does not exist");

            if (role == null)
                throw new Exception("Role not found");

            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);

            return new UserResponse("User role updated successfully", user);
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), null);
        }
    }

    public DefaultResponse resetPassword(String username, String baseUrl) {
        try {
            ApplicationUser user = userRepository.findByUsername(username).get();

            String token = createPasswordResetToken(username, user);
            // TODO: Send actual email using mail provider
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

    public UserResponse changePasswordWithToken(String token, String password) {
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

            return new UserResponse("Password changed", user);

        } catch (Exception e) {
            return new UserResponse(e.getMessage().length() > 30
                    ? "Request failed"
                    : e.getMessage(), null);
        }
    }

}
