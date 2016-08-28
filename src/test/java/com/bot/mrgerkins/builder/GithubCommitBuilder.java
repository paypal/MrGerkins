package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubCommit;


/**
 * Created by biphilip on 7/17/16.
 */
public class GithubCommitBuilder {

    private GithubCommit githubCommit;

    public GithubCommitBuilder(){
        githubCommit = new GithubCommit();
    }

    public GithubCommitBuilder message(String message){
        githubCommit.setMessage(message);
        return this;
    }

    public GithubCommit build(){
        return githubCommit;
    }

}
