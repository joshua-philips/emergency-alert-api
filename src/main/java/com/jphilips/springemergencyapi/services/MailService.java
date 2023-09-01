package com.jphilips.springemergencyapi.services;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.jphilips.springemergencyapi.models.user.ApplicationUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final Environment env;
    // private final JavaMailSender mailSender;

    public SimpleMailMessage constructResetTokenEmail(
            String baseUrl, String token, ApplicationUser user) {
        String url = baseUrl + "/auth/change-password/" + token;
        String message = "Click the link to reset your password";

        SimpleMailMessage mail = constructEmail("Reset Password", message + " \r\n" + url, user);

        // mailSender.send(mail);
        System.out.println(mail);
        return mail;
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
