package com.bot.mrgerkins.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@ConfigurationProperties(prefix = "github")
@Component
public class GithubConfig {

    private String clientId;
    private String homepageUrl;
    private String callbackUrl;
    private String apiUrl;
    private String accessToken;
    private String teamMembers;
    private GithubAuthorConfig author;

    public HashSet<String> getTeamMembers() {
        HashSet<String> teamMembersSet = new HashSet<String>();
        String []members = teamMembers.split(",");
        for(String member:members){
            teamMembersSet.add(member);
        }
        return teamMembersSet;
    }

    public void setTeamMembers(String teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public GithubAuthorConfig getAuthor() {
        return author;
    }

    public void setAuthor(GithubAuthorConfig author) {
        this.author = author;
    }
}
