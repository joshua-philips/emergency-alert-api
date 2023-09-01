package com.jphilips.springemergencyapi.dto.users;

import com.jphilips.springemergencyapi.models.user.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
    private String message;
    private Iterable<ApplicationUser> users;
}
