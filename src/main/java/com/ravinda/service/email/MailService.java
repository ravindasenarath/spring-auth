package com.ravinda.service.email;


import org.springframework.mail.SimpleMailMessage;

public interface MailService {

    void sendEmail(SimpleMailMessage message);
}
