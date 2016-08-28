package com.bot.mrgerkins.domain.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by biphilip on 6/4/16.
 */
@Component
public class TemplateUtils {

    public String readTemplateFile(String filePath) {
        String fileContent = String.valueOf("");
        BufferedReader buf = null;
        try {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext();
            Resource resource = applicationContext.getResource(filePath);
            buf = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            fileContent = buf.lines().collect(Collectors.joining("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return fileContent;
    }

    public String materializeTemplateFile(String template, HashMap<String, String> context) {
        List<String> findList = new ArrayList<>();
        List<String> replaceList = new ArrayList<>();

        context.forEach((key, value) -> {
            findList.add("$"+key);
            replaceList.add(value);
        });
        return StringUtils.replaceEach(template, findList.stream().toArray(String[]::new),
                replaceList.stream().toArray(String[]::new));
    }

}
