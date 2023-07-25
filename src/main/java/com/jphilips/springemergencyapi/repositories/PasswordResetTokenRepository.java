package com.jphilips.springemergencyapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.PasswordResetToken;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
