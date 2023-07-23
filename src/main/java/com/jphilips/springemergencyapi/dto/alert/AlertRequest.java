package com.jphilips.springemergencyapi.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertRequest {
    private String username;
    private Double longitude;
    private Double latutide;
    private String town;
    private Long status_id;
}
