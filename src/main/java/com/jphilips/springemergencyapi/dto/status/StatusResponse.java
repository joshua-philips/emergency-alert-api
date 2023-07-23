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
public class StatusResponse {
    private String message;
    private Status status;
}
