package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by biphilip on 5/21/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubContents {

    private String name;
    private String path;
    private String sha;
    private String content;
}
