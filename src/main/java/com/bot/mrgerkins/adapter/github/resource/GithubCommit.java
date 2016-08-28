package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by biphilip on 5/17/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubCommit {

    private String message;

}
