package com.jphilips.springemergencyapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.status.AllStatusResponse;
import com.jphilips.springemergencyapi.dto.status.StatusRequest;
import com.jphilips.springemergencyapi.dto.status.StatusResponse;
import com.jphilips.springemergencyapi.services.StatusService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class StatusController {
    private final StatusService statusService;

    @GetMapping("/")
    public AllStatusResponse listStatuses() {
        return statusService.listAllStatuses();
    }

    @PostMapping("/create")
    public StatusResponse createStatus(@RequestBody StatusRequest request) {
        return statusService.addStatus(request.getStatus_name());
    }

}
