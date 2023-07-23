package com.jphilips.springemergencyapi.dto.users;

import com.jphilips.springemergencyapi.models.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String message;
    private ApplicationUser user;
}
