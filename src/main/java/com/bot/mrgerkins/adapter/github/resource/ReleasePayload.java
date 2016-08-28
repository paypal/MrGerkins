package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by biphilip on 5/17/16.
 *
 * Webhooks payload send by Github
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ReleasePayload {
    private  String action;
    private GithubRelease release;
    private GithubRepo repository;
}
