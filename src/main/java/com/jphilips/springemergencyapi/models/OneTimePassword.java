package com.jphilips.springemergencyapi.models;

import java.time.LocalDateTime;

import com.jphilips.springemergencyapi.models.user.ApplicationUser;
import com.jphilips.springemergencyapi.models.user.StaffUser;

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

    private String code;

    @OneToOne
    private ApplicationUser user;

    @OneToOne
    private StaffUser staffUser;

    private LocalDateTime expiryDate;

    public OneTimePassword(String code) {
        this.code = code;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public OneTimePassword(String code, ApplicationUser user) {
        this.code = code;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public OneTimePassword(String code, StaffUser staffUser) {
        this.code = code;
        this.staffUser = staffUser;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public int getExpiration() {
        return EXPIRATION;
    }

    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }
}
