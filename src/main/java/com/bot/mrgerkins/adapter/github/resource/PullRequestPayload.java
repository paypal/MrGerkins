package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestPayload {

    private String action;
    private Integer number;
    private GithubRepo repository;

    @JsonProperty("pull_request")
    private GithubPullRequest pullRequest;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public GithubPullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(GithubPullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    public GithubRepo getRepository() {
        return repository;
    }

    public void setRepository(GithubRepo repository) {
        this.repository = repository;
    }

}
