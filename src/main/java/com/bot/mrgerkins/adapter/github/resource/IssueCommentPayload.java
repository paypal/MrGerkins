package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class IssueCommentPayload {

    private String action;
    private Integer number;
    private GithubRepo repository;
    private GithubComment coment;

}
