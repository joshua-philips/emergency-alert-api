package com.jphilips.springemergencyapi.controllers.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jphilips.springemergencyapi.dto.DefaultResponse;
import com.jphilips.springemergencyapi.dto.status.StatusRequest;
import com.jphilips.springemergencyapi.services.StatusService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staff/status")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class StatusController {
    private final StatusService statusService;

    @GetMapping("/")
    public DefaultResponse listStatuses() {
        return new DefaultResponse("Statuses loaded", statusService.listAllStatuses());
    }

    @PostMapping("/create")
    public DefaultResponse createStatus(@RequestBody StatusRequest request) {
        return new DefaultResponse("Status created", statusService.addStatus(request.getStatus_name()));
    }

}
