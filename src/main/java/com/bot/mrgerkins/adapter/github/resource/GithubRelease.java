package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by biphilip on 5/17/16.
 *
 * Resource returned by GET /repos/:owner/:repo/releases/latest
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubRelease {

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("target_commitish")
    private String targetCommitish; /*target branch*/

    @JsonProperty("html_url")
    private String htmlUrl;

    private String id;
    private String name;
    private String body;

}
