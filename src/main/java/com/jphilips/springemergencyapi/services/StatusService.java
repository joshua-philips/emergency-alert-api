package com.jphilips.springemergencyapi.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

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

    public Status addStatus(String statusName) {
        return statusRepository.save(new Status(0L, statusName));
    }

    public Iterable<Status> listAllStatuses() {
        return statusRepository.findAll();
    }

    public boolean statusAlreadyExists(String statusName) {
        Iterable<Status> listAllStatuses = listAllStatuses();
        Iterator<Status> iterator = listAllStatuses.iterator();

        while (iterator.hasNext()) {
            Status currentStatus = iterator.next();
            if (currentStatus.getStatus_name().toLowerCase().equals(statusName.toLowerCase())) {
                return true;
            }
        }
        return false;

    }

}
