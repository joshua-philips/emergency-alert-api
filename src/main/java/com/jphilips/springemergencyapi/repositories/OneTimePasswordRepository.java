package com.jphilips.springemergencyapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.OneTimePassword;

public interface OneTimePasswordRepository extends CrudRepository<OneTimePassword, Long> {

    Optional<OneTimePassword> findByToken(String token);

}
