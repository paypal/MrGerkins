package com.bot.mrgerkins.domain.utils;

import com.bot.mrgerkins.adapter.github.resource.*;
import com.bot.mrgerkins.adapter.email.EmailClientWrapper;
import com.bot.mrgerkins.adapter.github.GithubClientWrapper;
import com.bot.mrgerkins.config.EmailConfig;
import com.bot.mrgerkins.config.GithubConfig;
import com.bot.mrgerkins.domain.constants.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.mail.EmailException;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by bijilap on 5/18/16.
 */
@Component
public class GithubUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String MERGE_PULL_REQUEST_MSG_REGEX = "Merge pull request #(\\d+) from (\\w+)/.+\n\n(.+)";
    private static final String RELEASE_NOTES_FILE_PATH = "RELEASE_NOTES.md";
    private static final String HEAD_TAG = "HEAD";
    private static final String NOTIFY_TAG_PR_AUTHORS_TEMPLATE_PATH = "email/notifyTagPRAuthors.html";
    private static final String NOTIFY_RELEASE_TEMPLATE_PATH = "email/notifyReleaseComplete.html";
    private static final String NOTIFY_TAG_DL_TEMPLATE_PATH = "email/notifyTagDL.html";

    @Autowired
    GithubClientWrapper githubClientWrapper;

    @Autowired
    EmailClientWrapper emailClientWrapper;

    @Autowired
    GithubConfig githubConfig;

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    private TemplateUtils templateUtils;

    /**
     * Get list of Pull requests that we merged after last tag
     *
     * @param repoFullName
     * @throws HttpException
     * @throws IOException
     */
    public List<Triple<String, String, String>> getListOfMergedPRs(String repoFullName, String headTag)
            throws HttpException, IOException {
        GithubRelease releaseResponse = githubClientWrapper.getLatestRelease(repoFullName);
        GithubCompareTag compareTagResponse = githubClientWrapper.compareCommitTags(repoFullName, releaseResponse.getTagName(),
                headTag);

        Pattern pattern = Pattern.compile(MERGE_PULL_REQUEST_MSG_REGEX);

        List<GithubCommitRecord> commitRecords = compareTagResponse.getCommits();

        //Triple <PR#>
        List<Triple<String, String, String>> prList = new ArrayList<>();

        for (GithubCommitRecord commitRecord : commitRecords) {
            String message = commitRecord.getCommit().getMessage();
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches() && matcher.groupCount() == 3) {
                prList.add(Triple.of(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return prList;
    }

    public void updateFileInRepo(String repoFullName, String filePath, String newFileContent, String branch)
            throws IOException,
            HttpException {
        GithubContents fileDetails = githubClientWrapper.getContents(repoFullName, filePath, branch);

        String updateFileRequest = Json.createObjectBuilder()
                .add("message", new StringBuilder("Updating ").append(filePath).toString())
                .add("content", Base64.getEncoder().encodeToString(newFileContent.getBytes()))
                .add("sha", fileDetails.getSha())
                .add("committer", Json.createObjectBuilder()
                        .add("name", githubConfig.getAuthor().getName())
                        .add("email", githubConfig.getAuthor().getEmail())
                        .build()
                )
                .add("branch", branch)
                .build().toString();
        githubClientWrapper.updateFile(repoFullName, filePath, updateFileRequest);
    }

    /**
     * repofullname -> ex: Payments-R/paymentapiplatformserv
     */
    public void generateReleaseNotes(String repoFullName, String branch, String releaseVersion) throws HttpException,
            IOException {

        /**
         * get all the commits after last release tag
         */
        List<Triple<String, String, String>> mergedPRs = getListOfMergedPRs(repoFullName, branch);
        StringBuilder notes = new StringBuilder("## ").append(repoFullName).append(" &nbsp;&nbsp;&nbsp;&nbsp; v")
                .append(releaseVersion).append("\n### Changes \n");

        // Each line of release notes will be of form : <a href = "PR link"> #PR-number </a> Title of PR
        mergedPRs.forEach(
                pr -> notes.append("\n* [#").append(pr.getLeft()).append("](").append(githubConfig.getHomepageUrl())
                        .append(repoFullName).append("/pull/").append(pr.getLeft()).append(")&nbsp;&nbsp;&nbsp;&nbsp;")
                        .append(pr.getRight()));

        updateFileInRepo(repoFullName, RELEASE_NOTES_FILE_PATH, notes.toString(), branch);
    }

    /**
     * Notify PR contributors that their PRs are part of the given tag
     * @param repoFullName
     * @param branch
     * @param releaseVersion
     * @param newTag
     * @throws HttpException
     * @throws IOException
     */
    public void notifyTagPRAuthors(String repoFullName, String branch, String releaseVersion, String newTag)
            throws HttpException, IOException {

        /**
         * get all the commits after last release tag
         */

        String emailTemplate = templateUtils.readTemplateFile(NOTIFY_TAG_PR_AUTHORS_TEMPLATE_PATH);

        List<Triple<String, String, String>> mergedPRs = getListOfMergedPRs(repoFullName, newTag);
        HashMap<String, List<HashMap<String, String>>> authorAndPRs = new HashMap<String, List<HashMap<String, String>>>();

        mergedPRs.forEach(pr -> {
            HashMap<String, String> record = new HashMap<String, String>();
            record.put("number", pr.getLeft());
            record.put("title", pr.getRight());
            if (authorAndPRs.get(pr.getMiddle()) == null) {
                authorAndPRs.put(pr.getMiddle(), new ArrayList<HashMap<String, String>>() {{
                    add(record);
                }});
            }
            else {
                authorAndPRs.get(pr.getMiddle()).add(record);
            }

        });

        authorAndPRs.forEach((author, prs) -> {
            try {
                logger.info(author);
                HashMap<String, String> context = new HashMap();
                context.put("releaseVersion", releaseVersion);
                context.put("repository", repoFullName);

                StringBuilder prListHtml = new StringBuilder("<ul>");
                prs.forEach(pr ->
                        prListHtml.append("<li><a href=\"").append(githubConfig.getHomepageUrl()).append(repoFullName)
                                .append("/pull/").append(pr.get("number")).append("\">").append(pr.get("number"))
                                .append("</a>").append("&nbsp;&nbsp;&nbsp;").append(pr.get("title")).append("</li>")
                );
                prListHtml.append("</ul>");
                context.put("prList", prListHtml.toString());

                emailClientWrapper
                        .sendMail(emailConfig.getSender(),
                                new StringBuilder(author).append("@").append(emailConfig.getDomain()).toString(),
                                repoFullName + " Pull Request Update",
                                templateUtils.materializeTemplateFile(emailTemplate, context));
            }
            catch (EmailException e) {
                e.printStackTrace();
            }
        });

    }


    public void notifyTagCreationDL(String repoFullName, String releaseVersion, String buildId, String notifyDL)
            throws HttpException, IOException {

        String emailTemplate = templateUtils.readTemplateFile(NOTIFY_TAG_DL_TEMPLATE_PATH);
        HashMap<String, String> context = new HashMap();
        String tagDetailsMessage = "";
        String buildDetailsMessage = "";

        if(StringUtils.isNotEmpty(releaseVersion)){
            tagDetailsMessage = String.format("<p>A new tag %s has been created for %s.</p>", releaseVersion, repoFullName);
        }
        if(StringUtils.isNotEmpty(buildId)){
            buildDetailsMessage = String.format("<p>A new build: %s has been created for %s.</p>", buildId, repoFullName);
        }

        context.put("tagDetailsMessage", tagDetailsMessage);
        context.put("buildDetailsMessage", buildDetailsMessage);

        try {
            emailClientWrapper
                    .sendMail(emailConfig.getSender(),
                            notifyDL,
                            String.format("New Tag/Build created for %s", repoFullName),
                            templateUtils.materializeTemplateFile(emailTemplate, context));
        }
        catch (EmailException e) {
            logger.info(ErrorMessage.EMAIL_EXCEPTION.getMessage());
        }

    }

    public void notifyRelease(ReleasePayload request) throws EmailException {

        final String repoFullName = request.getRepository().getFullName();
        final String tagName = request.getRelease().getTagName();

        HashMap<String, String> context = new HashMap<>();
        context.put("repository", repoFullName);
        context.put("releaseInfo", request.getRelease().getHtmlUrl());
        context.put("tag", tagName);
        context.put("releaseUrl", new StringBuilder(request.getRepository().getHtmlUrl()).append("/blob/").append(tagName).append("/").append(RELEASE_NOTES_FILE_PATH).toString());
        emailClientWrapper
                .sendMail(emailConfig.getSender(),
                        emailConfig.getReleaseNotifyDlForRepo(repoFullName),
                        repoFullName + " Release Update",
                        templateUtils
                                .materializeTemplateFile(templateUtils.readTemplateFile(NOTIFY_RELEASE_TEMPLATE_PATH),
                                        context));

    }
}
