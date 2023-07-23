package com.jphilips.springemergencyapi.dto.role;

import com.jphilips.springemergencyapi.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllRolesResponse {
    private String message;
    private Iterable<Role> roles;
}
