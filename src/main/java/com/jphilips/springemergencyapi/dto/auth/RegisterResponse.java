package com.jphilips.springemergencyapi.dto.auth;

import com.jphilips.springemergencyapi.models.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private String token;
    private ApplicationUser user;
}
