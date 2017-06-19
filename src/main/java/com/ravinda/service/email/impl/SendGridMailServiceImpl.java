package com.ravinda.service.email.impl;

import com.ravinda.service.email.MailService;
import com.sendgrid.SendGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SendGridMailServiceImpl implements MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    final static String SENDGRID_API_KEY = "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";
    final static String SENDGRID_SENDER = "ravindasenarath@gmail.com";


    @Override
    public void sendEmail(SimpleMailMessage message) {
        try {
            SendGrid sendgrid = new SendGrid(SENDGRID_API_KEY);
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(message.getTo());
            email.setFrom(SENDGRID_SENDER);
            email.setSubject(message.getSubject());
            email.setText(message.getText());

            SendGrid.Response response = sendgrid.send(email);
            if (response.getCode() != 200) {
                LOGGER.error(String.format("An error occured: %s", response.getMessage()));
                return;
            }
        } catch (Exception e){

        }
    }
}
