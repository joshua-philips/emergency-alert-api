package com.jphilips.springemergencyapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);

}
