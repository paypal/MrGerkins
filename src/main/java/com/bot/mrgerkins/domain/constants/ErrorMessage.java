package com.bot.mrgerkins.domain.constants;

/**
 * Created by bijilap on 8/3/16.
 */
public enum ErrorMessage {
    HTTP_EXCEPTION("An http exception occurred while connecting to the server."),
    HTTP_IO_EXCEPTION("An io exception occurred while reading server response."),
    EMAIL_EXCEPTION("An exception occured while sending out the email");

    private String message;
    ErrorMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return  message;
    }
}
