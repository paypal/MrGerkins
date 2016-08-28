package com.bot.mrgerkins.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix = "fusion")
@Component
public class FusionConfig {

    private String apiPrefix;
    private String user;
    private String accessToken;
    private List<String> repositories;

    public String getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        System.out.println(user);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, String> getRepositories() {
        Map<String, String> records = new HashMap<>();
        for(String repo:repositories){
            String[] record = repo.split(":");
            System.out.println(record.length);
            records.put(record[0], record[1]);
        }
        return records;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }
}
