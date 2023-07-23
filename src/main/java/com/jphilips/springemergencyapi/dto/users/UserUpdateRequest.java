package com.jphilips.springemergencyapi.dto.users;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Nullable
    private String username;
    @Nullable
    private Long user_id;
    @Nullable
    private String password;
    @Nullable
    private String first_name;
    @Nullable
    private String last_name;
    @Nullable
    private Long role_id;
}
