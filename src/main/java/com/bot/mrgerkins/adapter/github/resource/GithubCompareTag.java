package com.bot.mrgerkins.adapter.github.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Created by biphilip on 5/17/16.
 *
 * Resource for response from GET /compare
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubCompareTag {

    private String status;

    @JsonProperty("ahead_by")
    private Integer aheadBy;

    @JsonProperty("behind_by")
    private Integer behindBy;

    private List<GithubCommitRecord> commits;
}
