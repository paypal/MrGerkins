package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubRepo;


/**
 * Created by biphilip on 7/17/16.
 */
public class GithubRepoBuilder {

    private GithubRepo githubRepo;

    public GithubRepoBuilder() {
        githubRepo = new GithubRepo();
    }

    public GithubRepoBuilder name(String name) {
        githubRepo.setName(name);
        return this;
    }

    public GithubRepoBuilder fullName(String fullName) {
        githubRepo.setFullName(fullName);
        return this;
    }

    public GithubRepoBuilder htmlUrl(String htmlUrl){
        githubRepo.setHtmlUrl(htmlUrl);
        return this;
    }

    public GithubRepo build() {
        return githubRepo;
    }
}
