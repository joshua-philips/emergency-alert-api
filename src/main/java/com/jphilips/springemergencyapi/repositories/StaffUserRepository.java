package com.jphilips.springemergencyapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.user.StaffUser;

public interface StaffUserRepository extends CrudRepository<StaffUser, Long> {
    Optional<StaffUser> findByUsername(String username);

}
