package com.bot.mrgerkins.adapter.email;

import com.bot.mrgerkins.config.EmailConfig;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by bijilap on 5/24/16.
 */
@Service
public class EmailClientWrapperImpl implements EmailClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    EmailConfig emailConfig;

    @Override
    public void sendMail(String sender, String recipient, String subject, String message) throws EmailException {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(emailConfig.getSmtpHost());
        email.setSmtpPort(emailConfig.getSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator("", ""));
        email.setFrom(sender);
        email.setSubject(subject);
        email.setMsg(message);
        email.setHtmlMsg(message);
        email.addTo(recipient);
        email.send();

        logger.info(String.format("Email sent to: %s", recipient));
    }
}
