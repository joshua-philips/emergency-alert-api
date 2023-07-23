package com.jphilips.springemergencyapi.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.role.AllRolesResponse;
import com.jphilips.springemergencyapi.dto.role.RoleResponse;
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

    public RoleResponse addRole(String roleName) {
        if (roleAlreadyExists(roleName)) {
            return new RoleResponse(roleName + " Already exists", null);
        }
        return new RoleResponse("Role loaded", roleRepository.save(new Role(0L, roleName)));
    }

    public AllRolesResponse listAllRoles() {
        return new AllRolesResponse("Roles loaded", roleRepository.findAll());
    }

    public boolean roleAlreadyExists(String roleName) {
        AllRolesResponse list = listAllRoles();
        Iterator<Role> iterator = list.getRoles().iterator();

        while (iterator.hasNext()) {
            Role currentStatus = iterator.next();
            if (currentStatus.getAuthority().toLowerCase().equals(roleName.toLowerCase())) {
                return true;
            }
        }
        return false;

    }
}
