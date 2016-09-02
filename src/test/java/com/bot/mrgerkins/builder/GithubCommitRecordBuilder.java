package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubCommit;
import com.bot.mrgerkins.adapter.github.resource.GithubCommitRecord;
import com.bot.mrgerkins.adapter.github.resource.GithubRelease;


/**
 * Created by bijilap on 7/17/16.
 */
public class GithubCommitRecordBuilder {

    private GithubCommitRecord githubCommitRecord;

    public GithubCommitRecordBuilder(){
        githubCommitRecord = new GithubCommitRecord();
    }

    public GithubCommitRecordBuilder commit(GithubCommit commit){
        githubCommitRecord.setCommit(commit);
        return this;
    }

    public GithubCommitRecord build(){
        return githubCommitRecord;
    }

}
