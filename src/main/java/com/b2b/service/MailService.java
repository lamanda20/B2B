package com.b2b.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String token) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Password reset token");

        msg.setText("""
        Hello,

        Use the following token to reset your password:

        %s

        If you did not request this, you can ignore this email.

        — B2B Platform
        """.formatted(token));

        mailSender.send(msg);
    }
}
