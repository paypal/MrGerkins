package com.bot.mrgerkins.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


/**
 * Created by biphilip on 8/3/16.
 */
public class LoggingUtils {

    private final static String EMPTY  = "EMPTY";

    public static String logPayload(Object payload){
        if(payload == null){
            return  EMPTY;
        }
        return sanitizeString(payload.toString());
    }

    public static String logPayload(String payload){
        if(StringUtils.isEmpty(payload)){
            return  EMPTY;
        }
        return payload.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
    }

    private static String sanitizeString(String input){
        return input.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
    }
}
