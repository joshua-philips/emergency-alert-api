package com.jphilips.springemergencyapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.alert.AlertRequest;
import com.jphilips.springemergencyapi.dto.alert.AlertResponse;
import com.jphilips.springemergencyapi.models.Alert;
import com.jphilips.springemergencyapi.repositories.AlertRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final StatusService statusService;

    public Iterable<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public AlertResponse getAlert(Long id) {
        Optional<Alert> alert = alertRepository.findById(id);
        if (alert.isPresent()) {
            return new AlertResponse("Alert loaded successfuly", alert.get());
        }

        return new AlertResponse("Alert not found", null);
    }

    public AlertResponse addAlert(AlertRequest request) {
        try {
            Alert alert = Alert.builder()
                    .username(request.getUsername())
                    .status(statusService.getStatusById(request.getStatus_id()))
                    .longitude(request.getLongitude())
                    .latutide(request.getLatutide())
                    .town(request.getTown())
                    .date_created(LocalDateTime.now())
                    .date_updated(LocalDateTime.now())
                    .build();

            Alert savedAlert = alertRepository.save(alert);
            return new AlertResponse("Alert created successfuly", savedAlert);
        } catch (Exception e) {
            return new AlertResponse(e.getMessage(), null);
        }

    }

}
