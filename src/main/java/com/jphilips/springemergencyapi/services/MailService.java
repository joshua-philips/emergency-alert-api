package com.jphilips.springemergencyapi.services;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final Environment env;
    // private final JavaMailSender mailSender;

    public SimpleMailMessage constructResetTokenEmail(
            String baseUrl, String token, String emailAddress, boolean isStaff) {
        String url = baseUrl + (isStaff ? "/staff" : "/user") + "/change-password/" + token;
        String message = "Click the link to reset your password";

        SimpleMailMessage mail = constructEmail("Reset Password", message + " \r\n" + url, emailAddress);

        // mailSender.send(mail);
        return mail;
    }

    public SimpleMailMessage registeredStaffEmail(
            String password, String email) {
        String message = String.format("%s\n%s", email, password);

        SimpleMailMessage mail = constructEmail("Staff registered successfully", message, email);

        // mailSender.send(mail);
        return mail;
    }

    public SimpleMailMessage sendMessage(String emailAddress, String subject, String body) {
        SimpleMailMessage mail = constructEmail(subject, body, emailAddress);

        // mailSender.send(mail);
        return mail;
    }

    private SimpleMailMessage constructEmail(String subject, String body,
            String emailAddress) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(emailAddress);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
