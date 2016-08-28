package com.bot.mrgerkins.adapter.github;

import com.bot.mrgerkins.config.GithubConfig;
import com.bot.mrgerkins.adapter.github.resource.GithubCompareTag;
import com.bot.mrgerkins.domain.utils.LoggingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bot.mrgerkins.adapter.github.resource.GithubContents;
import com.bot.mrgerkins.adapter.github.resource.GithubRelease;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GithubClientWrapperImpl implements GithubClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static String ACCESS_TOKEN_PARAM = "?access_token=";
    private static String LATEST_RELEASE_URI = "/releases/latest";
    private static String COMPARE_URI = "/compare/";
    private static String CONTENTS_URI = "/contents/";

    @Autowired
    private GithubConfig githubConfig;

    /**
     *  POST /repos/:owner/:repo/issues/:number/labels
     *  ["Label1","Label2"]
     */
    @Override
    public void labelPullRequest(String repoFullName, Integer issueNumber, String requestBody) {

        String requestUrl = new StringBuilder(githubConfig.getApiUrl()).append(repoFullName).append("/issues/").append(issueNumber).append("/labels")
                .append(ACCESS_TOKEN_PARAM).append(githubConfig.getAccessToken()).toString();

        logger.info(requestUrl);

        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
        try {
            HttpPost request =  new HttpPost(requestUrl);
            StringEntity params = new StringEntity(requestBody);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            logger.info("Github POST /labelPullRequest API Response status: {}", LoggingUtils.logPayload(response.getStatusLine()));

            // handle response here...
        } catch (Exception ex) {
            // handle exception here
            logger.info("Fail: Github API for Labeling PR");
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

    public GithubRelease getLatestRelease(String repoFullName) throws IOException, HttpException {
        String requestUrl = new StringBuilder(githubConfig.getApiUrl()).append(repoFullName).append(LATEST_RELEASE_URI)
                .append(ACCESS_TOKEN_PARAM).append(githubConfig.getAccessToken()).toString();

        return (GithubRelease) executeGetCall(requestUrl, GithubRelease.class);
    }

    public GithubCompareTag compareCommitTags(String repoFullName, String prevTag, String headTag)  throws IOException,
            HttpException {

        String requestUrl = new StringBuilder(githubConfig.getApiUrl()).append(repoFullName).append(COMPARE_URI)
                .append(prevTag).append("...").append(headTag).append(ACCESS_TOKEN_PARAM).append(
                        githubConfig.getAccessToken()).toString();

        return (GithubCompareTag) executeGetCall(requestUrl, GithubCompareTag.class);
    }

    public GithubContents getContents(String repoFullName, String filePath, String branch)  throws IOException,
            HttpException {

        String requestUrl = new StringBuilder(githubConfig.getApiUrl()).append(repoFullName).append(CONTENTS_URI)
                .append(filePath).append(ACCESS_TOKEN_PARAM).append(githubConfig.getAccessToken()).append("&ref=")
                .append(branch).toString();

        return (GithubContents) executeGetCall(requestUrl, GithubContents.class);
    }

    /**
     * Update a file using github api
     *
     * Sample Request:
     *   {
             "message": "my commit message",
             "committer": {
             "name": "Scott Chacon",
             "email": "schacon@gmail.com"
             },
             "content": "bXkgdXBkYXRlZCBmaWxlIGNvbnRlbnRz",
             "sha": "329688480d39049927147c162b9d2deaf885005f",
             "branch" : "optional branch field"
         }
     * @param repoFullName
     * @param filePath
     * @param requestBody
     */
    @Override
    public void updateFile(String repoFullName, String filePath, String requestBody) {

        String requestUrl = new StringBuilder(githubConfig.getApiUrl()).append(repoFullName).append(CONTENTS_URI)
                .append(filePath).append(ACCESS_TOKEN_PARAM).append(githubConfig.getAccessToken()).toString();

        logger.info(requestUrl);

        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        try {
            HttpPut request =  new HttpPut(requestUrl);
            StringEntity params = new StringEntity(requestBody);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            logger.info("Github PUT /updateFIle API Response status: {}", LoggingUtils.logPayload(response.getStatusLine()));
            logger.info(LoggingUtils.logPayload(request));
            logger.info(LoggingUtils.logPayload(response));

            // handle response here...
        } catch (Exception ex) {
            // handle exception here
            logger.info("Fail: Github API for Labeling PR");
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

    private Object executeGetCall(String requestUrl, Class<?> responseClass) throws IOException, HttpException{

        logger.info(requestUrl);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(requestUrl);
        HttpResponse response = httpClient.execute(request);


        logger.info("Github GET {} API Response status: {}", requestUrl , LoggingUtils.logPayload(response.getStatusLine()));

        if(HttpStatus.SC_OK != response.getStatusLine().getStatusCode()){
            throw new HttpException("Github GET call failed");
        }

        return new ObjectMapper().readValue(response.getEntity().getContent(), responseClass);

    }
}
