package com.bot.mrgerkins.adapter.dlmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.bot.mrgerkins.adapter.dlmanager.resource.ModelUser;
import com.bot.mrgerkins.domain.utils.TrustAllCertificates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.bot.mrgerkins.config.DLManagerConfig;


@Component
public class DLManagerClientWrapperImpl implements DLManagerClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String GET_MEMBERS_RECURSIVE_URI = "/API/DL/members/recursive/";

    @Autowired
    private DLManagerConfig dlManagerConfig;

    @Override
    public HashMap<String, ModelUser> getMembersRecursive() {
        String requestUrl = new StringBuilder(dlManagerConfig.getServiceUrl()).append(GET_MEMBERS_RECURSIVE_URI).
                append(dlManagerConfig.getTargetDl()).append("/").append(dlManagerConfig.getExecutor()).append("/").
                append(dlManagerConfig.getToken()).toString();

        //ignore self signed SSL certificates 
        TrustAllCertificates.install();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = buf.readLine()) != null) {
                response.append(line);
            }
            logger.debug("Github API Response status: {}", response);

            HashMap<String, ModelUser> userMap = new HashMap<String, ModelUser>();
            ArrayNode responseArray = (ArrayNode) new ObjectMapper().readTree(response.toString());
            for (JsonNode record : responseArray) {
                ModelUser user = new ObjectMapper().readValue(new TreeTraversingParser(record), ModelUser.class);
                userMap.put(user.getSamAccountName(), user);
            }
            connection.disconnect();
            logger.info("Success: DL Manager API for recursively getting members");

            return userMap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.info("Fail: DL Manager API for recursively getting members");
            e.printStackTrace();
        }
        return null;
    }
}
