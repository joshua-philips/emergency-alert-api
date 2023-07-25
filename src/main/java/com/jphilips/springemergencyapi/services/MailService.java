package com.jphilips.springemergencyapi.services;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.models.ApplicationUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final Environment env;

    public SimpleMailMessage constructResetTokenEmail(
            String baseUrl, String token, ApplicationUser user) {
        String url = baseUrl + "/auth/change-password/" + token;
        String message = "Click the link to reset your password";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
            ApplicationUser user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getUsername());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
