package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubRelease;


/**
 * Created by bijilap on 7/16/16.
 */
public class GithubReleaseBuilder {

    private GithubRelease githubRelease;

    public GithubReleaseBuilder(){
        githubRelease = new GithubRelease();
    }

    public GithubReleaseBuilder tagName(String tagName){
        githubRelease.setTagName(tagName);
        return this;
    }

    public GithubRelease build(){
        return githubRelease;
    }

}
