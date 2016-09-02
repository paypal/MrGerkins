package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by bijilap on 5/24/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubComment {

    private String id;
    private GithubUser user;
    private String body;

}
