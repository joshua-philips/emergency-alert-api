package com.jphilips.springemergencyapi.controllers.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.services.AlertService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staff/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class StaffAlertController {
    private final AlertService alertService;

    @GetMapping("/")
    public DefaultResponse getAllAlerts() {
        return new DefaultResponse("Alerts Loaded", alertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public DefaultResponse getAlert(@PathVariable Long id) {
        return new DefaultResponse("Alert Loaded", alertService.getAlert(id));
    }

}
