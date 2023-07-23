package com.jphilips.springemergencyapi.dto.alert;

import com.jphilips.springemergencyapi.models.Alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse {
    private String message;
    private Alert alert;
}
