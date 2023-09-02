package com.jphilips.springemergencyapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRegisterRequest {
    private String username;
    private String first_name;
    private String last_name;
    private String phone;
    private Long role_id;
}
