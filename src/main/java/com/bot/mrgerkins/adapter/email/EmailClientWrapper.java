package com.bot.mrgerkins.adapter.email;

import org.apache.commons.mail.EmailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * Created by bijilap on 5/24/16.
 */

public interface EmailClientWrapper {

    @Async
    public void sendMail(String sender, String recipient, String Subject, String body) throws EmailException;
}
