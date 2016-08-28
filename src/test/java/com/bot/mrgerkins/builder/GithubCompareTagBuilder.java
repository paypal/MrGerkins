package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubCommitRecord;
import com.bot.mrgerkins.adapter.github.resource.GithubCompareTag;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by biphilip on 7/17/16.
 */
public class GithubCompareTagBuilder {

    private GithubCompareTag githubCompareTag;

    public GithubCompareTagBuilder(){
        githubCompareTag = new GithubCompareTag();
    }

    public GithubCompareTagBuilder addCommit(GithubCommitRecord record){
        List<GithubCommitRecord> commitRecordList = githubCompareTag.getCommits();
        if(commitRecordList == null){
            commitRecordList = new ArrayList<>();
        }
        commitRecordList.add(record);
        githubCompareTag.setCommits(commitRecordList);
        return this;
    }

    public GithubCompareTag build(){
        return githubCompareTag;
    }
}
