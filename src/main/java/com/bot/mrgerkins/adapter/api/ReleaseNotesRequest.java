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
public class ReleaseNotesRequest {


    private String branch;

    @JsonProperty("release_version")
    private String releaseVersion;

    @JsonProperty("development_version")
    private String developmentVersion;

    @JsonProperty("repository_full_name")
    private String repositoryFullName;

    @JsonProperty("is_dry_run")
    private String isDryRun;

    @JsonProperty("scm_tag")
    private String scmTag;

    /*
    @JsonProperty("skip_tests")
    private String skipTests;
    */
    private String parameter;
}
