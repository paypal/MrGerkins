package com.bot.mrgerkins.adapter.jenkins;

import lombok.Getter;


/**
 * Created by bijilap on 6/18/16.
 */
@Getter
public enum M2ReleaseParams {
    RELEASE_VERSION("releaseVersion"),
    DEVELOP_VERSION("developmentVersion"),
    IS_DRY_RUN("isDryRun"),
    SPECIFY_SCM_TAG("specifyScmTag"),
    SCM_TAG("scmTag"),
    APPEND_HUDSON_BUILDER("appendHudsonBuildNumber"),
    PARAMETER("parameter"),
    JSON("json");

    String param;

    M2ReleaseParams(String param){
        this.param = param;
    }
}
