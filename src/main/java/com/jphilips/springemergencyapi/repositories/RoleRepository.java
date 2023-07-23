package com.jphilips.springemergencyapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
