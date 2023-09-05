package com.jphilips.springemergencyapi.dto.auth;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequest {
    @Nullable
    private String username;
    @Nullable
    private String token;
    @Nullable
    private String otp;

}
