package com.bot.mrgerkins.domain.utils;

import com.bot.mrgerkins.adapter.email.EmailClientWrapper;
import com.bot.mrgerkins.adapter.github.GithubClientWrapper;
import com.bot.mrgerkins.adapter.github.resource.GithubCommitRecord;
import com.bot.mrgerkins.adapter.github.resource.GithubCompareTag;
import com.bot.mrgerkins.adapter.github.resource.ReleasePayload;
import com.bot.mrgerkins.builder.*;
import com.bot.mrgerkins.config.EmailConfig;
import com.bot.mrgerkins.config.GithubConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.mail.EmailException;
import org.apache.http.HttpException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;


/**
 * Created by bijilap on 7/16/16.
 */
public class GithubUtilsTest {

    private static final String REPO_FULL_NAME = "bijilap/mr-gerkins";
    private static final String TAG_BRANCH = "master";
    private static final String LATEST_RELEASE_TAG = "v2.0.6";

    private static final String RELEASEEVENT_WH_FILE = "src/test/resources/github/ReleaseEventWebhook.json";
    private static final String GITHUB_URL = "github.com";

    @Mock
    private GithubClientWrapper githubClientWrapper;

    @Mock
    private EmailClientWrapper emailClientWrapper;

    @Mock
    private EmailConfig emailConfig;

    @Mock
    GithubConfig githubConfig;

    @Mock
    private TemplateUtils templateUtils;

    @Spy
    @InjectMocks
    private GithubUtils githubUtils;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test happy of method GithubUtils.getListOfMergedPRs
     * Given: List of commits added in the new tag since the last release tag
     * Expected: Pull request information obtained from commit message that satisfy the regex
     * @throws IOException
     * @throws HttpException
     */
    @Test
    public void testGetListOfMergedPRs() throws IOException, HttpException {
        mockGithubCompareTagResponse();

        //service calls
        //result: (PR#, Author, Title)
        List<Triple<String, String, String>> result =  githubUtils.getListOfMergedPRs(REPO_FULL_NAME, TAG_BRANCH);

        //assertions
        assertThat(result).hasSize(2);
        assertThat(result).extracting("left", "middle", "right").contains(tuple("12","bijilap","This is the title for PR"),
                tuple("72", "TabInsight", "Cleaning unused imports"));
    }

    /**
     * Test is if dl is notfied once webhook is recieved from github after release is drafted
     * @throws IOException
     * @throws EmailException
     */
    @Test
    public void testNotifyRelease() throws IOException, EmailException {
        String senderEmail = "bijil@mrgeerkins.com";
        String notifyDlEmail = "notify-dl@mrgeerkins.com";
        ReleasePayload releasePayload = new ObjectMapper().readValue(new String(Files.readAllBytes(Paths.get(RELEASEEVENT_WH_FILE))), ReleasePayload.class);

        Mockito.doAnswer(invocationOnMock -> {
            Object args[] = invocationOnMock.getArguments();
            assertThat(args).hasSize(4);
            assertThat(args).contains(senderEmail,notifyDlEmail, "Symphony Release Update");
            return invocationOnMock;
        }).when(emailClientWrapper).sendMail(anyString(), anyString(), anyString(), anyString());

        when(emailConfig.getSender()).thenReturn(senderEmail);
        when(emailConfig.getReleaseNotifyDlForRepo("baxterthehacker/public-repo")).thenReturn(notifyDlEmail);

        githubUtils.notifyRelease(releasePayload);
    }

    /**
     * Test if release notes are generated as expected
     * @throws IOException
     * @throws EmailException
     * @throws HttpException
     */
    @Test
    public void testGenerateReleaseNotes() throws IOException, EmailException, HttpException {
        String senderEmail = "bijil@mrgeerkins.com";
        String notifyDlEmail = "notify-dl@mrgeerkins.com";
        mockGithubCompareTagResponse();
        ReleasePayload releasePayload = new ObjectMapper().readValue(new String(Files.readAllBytes(Paths.get(RELEASEEVENT_WH_FILE))), ReleasePayload.class);

        when(githubConfig.getHomepageUrl()).thenReturn(GITHUB_URL);
        Mockito.doAnswer(invocationOnMock -> {
            Object args[] = invocationOnMock.getArguments();
            assertThat(args).hasSize(4);
            String expected = "## bijilap/mr-gerkins &nbsp;&nbsp;&nbsp;&nbsp; vv2.0.6\n"
                    + "### Changes \n"
                    + "\n"
                    + "* [#12](github.combijilap/mr-gerkins/pull/12)&nbsp;&nbsp;&nbsp;&nbsp;This is the title for PR\n"
                    + "* [#72](github.combijilap/mr-gerkins/pull/72)&nbsp;&nbsp;&nbsp;&nbsp;Cleaning unused imports";
            assertThat(args[2]).isEqualTo(expected);
            return invocationOnMock;
        }).when(githubUtils).updateFileInRepo(anyString(), anyString(), anyString(), anyString());


        githubUtils.generateReleaseNotes(REPO_FULL_NAME, TAG_BRANCH, LATEST_RELEASE_TAG);
    }

    private void mockGithubCompareTagResponse() throws IOException, HttpException {

        GithubCommitRecord githubCommitRecord1 = new GithubCommitRecordBuilder()
                .commit(new GithubCommitBuilder().message("Merge pull request #12 from bijilap/develop\n\nThis is the title for PR").build())
                .build();
        GithubCommitRecord githubCommitRecord2 = new GithubCommitRecordBuilder()
                .commit(new GithubCommitBuilder().message("Adding test changes #23").build())
                .build();
        GithubCommitRecord githubCommitRecord3 = new GithubCommitRecordBuilder()
                .commit(new GithubCommitBuilder().message("Merge pull request #72 from TabInsight/develop\n\nCleaning unused imports").build())
                .build();
        GithubCompareTag githubCompareTag = new GithubCompareTagBuilder().addCommit(githubCommitRecord1)
                .addCommit(githubCommitRecord2).addCommit(githubCommitRecord3).build();

        //define mocks and stubs
        when(githubClientWrapper.getLatestRelease(REPO_FULL_NAME))
                .thenReturn(new GithubReleaseBuilder().tagName(LATEST_RELEASE_TAG).build());
        when(githubClientWrapper.compareCommitTags(REPO_FULL_NAME, LATEST_RELEASE_TAG, TAG_BRANCH))
                .thenReturn(githubCompareTag);
    }
}


