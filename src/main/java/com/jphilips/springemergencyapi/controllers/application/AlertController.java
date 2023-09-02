package com.jphilips.springemergencyapi.controllers.application;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.alert.AlertRequest;
import com.jphilips.springemergencyapi.services.AlertService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/add")
    public DefaultResponse createAlert(@RequestBody AlertRequest body) {
        return new DefaultResponse("Alert added", alertService.addAlert(body));
    }

}
