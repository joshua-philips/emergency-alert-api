package com.jphilips.springemergencyapi.dto.status;

import com.jphilips.springemergencyapi.models.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllStatusResponse {
    private String message;
    private Iterable<Status> statuses;
}
