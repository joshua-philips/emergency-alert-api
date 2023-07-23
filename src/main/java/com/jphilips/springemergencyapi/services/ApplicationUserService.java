package com.jphilips.springemergencyapi.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.users.UserResponse;
import com.jphilips.springemergencyapi.dto.users.UserUpdateRequest;
import com.jphilips.springemergencyapi.dto.users.UsersResponse;
import com.jphilips.springemergencyapi.models.ApplicationUser;
import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.repositories.ApplicationUserRepository;
import com.jphilips.springemergencyapi.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository userRepository;
    private final RoleRepository roleRepository;

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

}
