package com.jphilips.springemergencyapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jphilips.springemergencyapi.models.Alert;

public interface AlertRepository extends CrudRepository<Alert, Long> {
}
