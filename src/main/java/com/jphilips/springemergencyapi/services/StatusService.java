package com.jphilips.springemergencyapi.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.dto.status.AllStatusResponse;
import com.jphilips.springemergencyapi.dto.status.StatusResponse;
import com.jphilips.springemergencyapi.models.Status;
import com.jphilips.springemergencyapi.repositories.StatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public Status getStatusById(Long id) {
        return statusRepository.findById(id).get();
    }

    public StatusResponse addStatus(String statusName) {
        if (statusAlreadyExists(statusName)) {
            return new StatusResponse(statusName + " Already exists", null);
        }
        return new StatusResponse("Status loaded", statusRepository.save(new Status(0L, statusName)));
    }

    public AllStatusResponse listAllStatuses() {
        return new AllStatusResponse("Statuses loaded", statusRepository.findAll());
    }

    public boolean statusAlreadyExists(String statusName) {
        AllStatusResponse listAllStatuses = listAllStatuses();
        Iterator<Status> iterator = listAllStatuses.getStatuses().iterator();

        while (iterator.hasNext()) {
            Status currentStatus = iterator.next();
            if (currentStatus.getStatus_name().toLowerCase().equals(statusName.toLowerCase())) {
                return true;
            }
        }
        return false;

    }

}
