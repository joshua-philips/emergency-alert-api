package com.jphilips.springemergencyapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.Status;

public interface StatusRepository extends CrudRepository<Status, Long> {
}
