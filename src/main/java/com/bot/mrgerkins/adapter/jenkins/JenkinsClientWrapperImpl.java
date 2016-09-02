package com.bot.mrgerkins.adapter.jenkins;

import com.bot.mrgerkins.config.FusionConfig;
import com.bot.mrgerkins.adapter.api.ReleaseNotesRequest;
import com.bot.mrgerkins.domain.utils.LoggingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


/**
 * Created by bijilap on 5/14/16.
 */
@Component
public class JenkinsClientWrapperImpl implements JenkinsClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String MAVEN_RELEASE_URI = "/m2release/submit";

    @Autowired
    private FusionConfig fusionConfig;

    public void kickOffTagBuild(ReleaseNotesRequest releaseNotesRequest) throws HttpException {

        String requestUrl = new StringBuilder(fusionConfig.getApiPrefix())
                .append(getFusionJobName(releaseNotesRequest.getRepositoryFullName()))
                .append(MAVEN_RELEASE_URI).toString();
        logger.info(requestUrl);

        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        /**
         * Sample Request:
         * {"releaseVersion": "1.0", "developmentVersion": "1.1-SNAPSHOT", "appendHudsonBuildNumber": false,
         * "isDryRun": false, "specifyScmTag": {"scmTag": "v1.0"},
         * "parameter": [{"name": "BRANCH", "value": "master"}, {"name": "SKIP_TESTS", "value": true}]}
         */

        try {
            HttpPost request = new HttpPost(requestUrl);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(M2ReleaseParams.RELEASE_VERSION.getParam(),
                    releaseNotesRequest.getReleaseVersion()));
            nameValuePairs
                    .add(new BasicNameValuePair(M2ReleaseParams.DEVELOP_VERSION.getParam(),
                            releaseNotesRequest.getDevelopmentVersion()));

            if("true".equals(releaseNotesRequest.getIsDryRun())) {
                nameValuePairs.add(new BasicNameValuePair(M2ReleaseParams.IS_DRY_RUN.getParam(),
                        releaseNotesRequest.getIsDryRun()));
            }

            if (StringUtils.isNotBlank(releaseNotesRequest.getScmTag())) {
                nameValuePairs.add(new BasicNameValuePair(M2ReleaseParams.SPECIFY_SCM_TAG.getParam(), "true"));
                nameValuePairs.add(new BasicNameValuePair(M2ReleaseParams.SCM_TAG.getParam(),
                        releaseNotesRequest.getScmTag()));
            }

            nameValuePairs.add(new BasicNameValuePair(M2ReleaseParams.JSON.getParam(),
                    new StringBuilder("{\"parameter\": ").append(releaseNotesRequest.getParameter()).append("}")
                            .toString()));



            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            logger.info(nameValuePairs.toString());

            String encodedUserCredentials = Base64.getEncoder().encodeToString(new StringBuilder(fusionConfig.getUser())
                    .append(":").append(fusionConfig.getAccessToken()).toString().getBytes());
            request.setHeader("Authorization", new StringBuilder("Basic ").append(encodedUserCredentials).toString());

            HttpResponse response = httpClient.execute(request);

            logger.info("Fusion API Response status: {}", LoggingUtils.logPayload(response.getStatusLine()));

            // handle response here...
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }

    }

    public void getLastBuildDetails() {

    }

    @Cacheable
    private String getFusionJobName(String repository) throws HttpException {
        Map<String, String> fusionJobs = fusionConfig.getRepositories();
        if (fusionJobs.get(repository) == null) {
            throw new HttpException("No fusion job for repo");
        }
        return fusionJobs.get(repository);
    }

}
