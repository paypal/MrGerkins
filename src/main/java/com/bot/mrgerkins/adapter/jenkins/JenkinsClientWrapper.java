package com.bot.mrgerkins.adapter.jenkins;

import com.bot.mrgerkins.adapter.api.ReleaseNotesRequest;
import org.apache.http.HttpException;


/**
 * Created by biphilip on 5/14/16.
 */
public interface JenkinsClientWrapper {

    public void kickOffTagBuild(ReleaseNotesRequest releaseNotesRequest) throws HttpException;
}
