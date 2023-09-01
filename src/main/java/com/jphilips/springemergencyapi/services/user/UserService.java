package com.jphilips.springemergencyapi.services.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.models.user.ApplicationUser;
import com.jphilips.springemergencyapi.models.user.StaffUser;
import com.jphilips.springemergencyapi.repositories.ApplicationUserRepository;
import com.jphilips.springemergencyapi.repositories.StaffUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final HttpServletRequest servletRequest;
    private final ApplicationUserRepository applicationUserRepository;
    private final StaffUserRepository staffUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (isStaffRequest(servletRequest.getRequestURI())) {
            StaffUser user = staffUserRepository.findByUsername(username)
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

        ApplicationUser user = applicationUserRepository.findByUsername(username)
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

    private boolean isStaffRequest(String requestUri) {
        if (requestUri.startsWith("/staff")) {
            return true;
        }
        return false;
    }

}
