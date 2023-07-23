package com.jphilips.springemergencyapi.dto.alert;

import com.jphilips.springemergencyapi.models.Alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertsResponse {
    private String message;
    private Iterable<Alert> alerts;
}
