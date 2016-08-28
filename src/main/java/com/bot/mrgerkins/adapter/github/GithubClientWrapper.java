package com.bot.mrgerkins.adapter.github;

import com.bot.mrgerkins.adapter.github.resource.GithubCompareTag;
import com.bot.mrgerkins.adapter.github.resource.GithubContents;
import com.bot.mrgerkins.adapter.github.resource.GithubRelease;
import org.apache.http.HttpException;

import java.io.IOException;


public interface GithubClientWrapper {

    public void labelPullRequest(String repoFullName, Integer issueNumber, String requestBody);

    public GithubRelease getLatestRelease(String repoFullName) throws IOException, HttpException;

    public GithubCompareTag compareCommitTags(String repoFullName, String prevTag, String headTag) throws IOException,
            HttpException;

    public GithubContents getContents(String repoFullName, String filePath, String branch)  throws IOException,
            HttpException;

    public void updateFile(String repoFullName, String filePath, String requestBody);

}
