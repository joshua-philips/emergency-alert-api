package com.jphilips.springemergencyapi.models;

import java.time.LocalDateTime;

import com.jphilips.springemergencyapi.models.user.ApplicationUser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OneTimePassword {
    private static final int EXPIRATION = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne
    private ApplicationUser user;
    private LocalDateTime expiryDate;

    public OneTimePassword(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public OneTimePassword(String token, ApplicationUser user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public int getExpiration() {
        return EXPIRATION;
    }

    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }
}

// public static String generateOTP(int length) {
// String numbers = "0123456789";
// Random rndm_method = new Random();
// char[] otp = new char[length];
// for (int i = 0; i < length; i++) {
// otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
// }
// return new String(otp);
// }