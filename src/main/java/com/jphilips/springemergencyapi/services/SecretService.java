package com.jphilips.springemergencyapi.services;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.io.Encoders;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Service
@Getter
public class SecretService {
    private String secret;

    @PostConstruct
    public void setup() {
        refreshSecret();
    }

    public void refreshSecret() {
        SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        secret = Encoders.BASE64.encode(key.getEncoded());
    }

}
