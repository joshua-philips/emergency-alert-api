package com.jphilips.springemergencyapi.services.user;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.models.OneTimePassword;
import com.jphilips.springemergencyapi.models.user.ApplicationUser;
import com.jphilips.springemergencyapi.models.user.StaffUser;
import com.jphilips.springemergencyapi.repositories.OneTimePasswordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OneTimePasswordRepository otpRepository;

    public String createOtp(ApplicationUser user) {
        String code = generateOTP(6);
        OneTimePassword otp = new OneTimePassword(code, user);
        otpRepository.save(otp);
        return otp.getCode();
    }

    public String createOtp(StaffUser user) {
        String code = generateOTP(6);
        OneTimePassword otp = new OneTimePassword(code, user);
        otpRepository.save(otp);
        return otp.getCode();
    }

    public static String generateOTP(int length) {
        String numbers = "0123456789";
        SecureRandom rndm_method = new SecureRandom();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public ApplicationUser verifyApplicationOtp(String code, String username) throws Exception {
        OneTimePassword otp = otpRepository.findByCode(code).get();
        ApplicationUser user = otp.getUser();

        if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Expired code");
        }

        if (!user.getUsername().equalsIgnoreCase(username)) {
            throw new Exception("Invalid code for " + username);

        }

        otp.setExpiryDate(LocalDateTime.now());
        otpRepository.delete(otp);
        return user;
    }

    public StaffUser verifyStaffOtp(String code, String username) throws Exception {

        OneTimePassword otp = otpRepository.findByCode(code).get();
        StaffUser user = otp.getStaffUser();

        if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Expired code");
        }

        if (!user.getUsername().equalsIgnoreCase(username)) {
            throw new Exception("Invalid code for " + username);

        }

        otp.setExpiryDate(LocalDateTime.now());
        otpRepository.delete(otp);
        return user;
    }
}
