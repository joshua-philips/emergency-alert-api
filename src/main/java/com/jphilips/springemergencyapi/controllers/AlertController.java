package com.jphilips.springemergencyapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.alert.AlertRequest;
import com.jphilips.springemergencyapi.dto.alert.AlertResponse;
import com.jphilips.springemergencyapi.dto.alert.AlertsResponse;
import com.jphilips.springemergencyapi.services.AlertService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/")
    public AlertsResponse getAllAlerts() {
        return new AlertsResponse("Alerts Loaded", alertService.getAllAlerts());
    }

    @PostMapping("/add")
    public AlertResponse createAlert(@RequestBody AlertRequest body) {
        return alertService.addAlert(body);
    }

    @GetMapping("/{id}")
    public AlertResponse getAlert(@PathVariable Long id) {
        return alertService.getAlert(id);
    }

}
