package com.bot.mrgerkins.adapter.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by bijilap on 5/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TagDLNotifyRequest {


    private String branch;

    @JsonProperty("repository_full_name")
    private String repositoryFullName;

    @JsonProperty("release_version")
    private String releaseVersion;

    @JsonProperty("build_id")
    private String buildId;

    @JsonProperty("notify_dl")
    private String notifyDl;

}
