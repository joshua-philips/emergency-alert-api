package com.jphilips.springemergencyapi.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.models.Role;
import com.jphilips.springemergencyapi.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RoleRepository roleRepository;

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).get();
    }

    public Role addRole(String roleName) {
        return roleRepository.save(new Role(0L, roleName));
    }

    public Iterable<Role> listAllRoles() {
        return roleRepository.findAll();
    }

    public boolean roleAlreadyExists(String roleName) {
        Iterator<Role> iterator = listAllRoles().iterator();

        while (iterator.hasNext()) {
            Role currentStatus = iterator.next();
            if (currentStatus.getAuthority().toLowerCase().equals(roleName.toLowerCase())) {
                return true;
            }
        }
        return false;

    }
}
