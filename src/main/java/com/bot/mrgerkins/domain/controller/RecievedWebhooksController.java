package com.bot.mrgerkins.domain.controller;

import com.bot.mrgerkins.adapter.jenkins.JenkinsClientWrapper;
import com.bot.mrgerkins.adapter.dlmanager.resource.DLMembersCollection;
import com.bot.mrgerkins.adapter.github.resource.IssueLabels;
import com.bot.mrgerkins.domain.constants.ErrorMessage;
import com.bot.mrgerkins.domain.utils.LoggingUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.bot.mrgerkins.adapter.dlmanager.DLManagerClientWrapper;
import com.bot.mrgerkins.adapter.github.GithubClientWrapper;
import com.bot.mrgerkins.config.DLManagerConfig;
import com.bot.mrgerkins.config.GithubConfig;
import com.bot.mrgerkins.adapter.dlmanager.resource.ModelUser;
import com.bot.mrgerkins.adapter.github.resource.Action;
import com.bot.mrgerkins.adapter.github.resource.PullRequestPayload;
import com.bot.mrgerkins.adapter.github.resource.ReleasePayload;
import com.bot.mrgerkins.domain.utils.GithubUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


@RestController
@RequestMapping("/webhook")
@EnableCaching
public class RecievedWebhooksController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public GithubConfig githubConfig;

    @Autowired
    private DLManagerConfig dlManagerConfig;

    @Autowired
    public DLManagerClientWrapper dLManagerClientWrapper;

    @Autowired
    private DLMembersCollection dLMembersCollection;

    @Autowired
    private GithubClientWrapper githubClientWrapper;

    @Autowired
    private JenkinsClientWrapper jenkinsClientWrapper;

    @Autowired
    private GithubUtils githubUtils;

    @RequestMapping(value = "/pull-request", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void recievePullRequestNotification(@RequestBody PullRequestPayload request) throws JsonParseException,
            JsonMappingException, IOException {
        logger.info("Recieved Webhook notification for Pull Request# {}", LoggingUtils.logPayload(request.getNumber()));
        logger.info(LoggingUtils.logPayload(request.getRepository().getFullName()));

        if(!Action.OPENED.equalsString(request.getAction())) {
            logger.info("Given action is not OPENED");
            return;
        }

        boolean isNotTeamMember = false;
        if (Boolean.TRUE == dlManagerConfig.getEnabled()) {
            logger.info("Using dl manager api");
            HashMap<String, ModelUser> dlMembersMap = dLMembersCollection.getDLMembersMap();
            isNotTeamMember = dlMembersMap.get(request.getPullRequest().getUser().getLogin()) == null;
        }
        else {
            logger.info("Using config for team members");
            HashSet<String> teamMembers = githubConfig.getTeamMembers();
            isNotTeamMember = !teamMembers.contains(request.getPullRequest().getUser().getLogin());
        }

        if (isNotTeamMember) {
            logger.info("Inner Sourced User {}", LoggingUtils.logPayload(request.getPullRequest().getUser().getLogin()));
            githubClientWrapper.labelPullRequest(request.getRepository().getFullName(), request.getNumber(),
                    new StringBuilder("[\"").append(IssueLabels.InnerSource.name()).append("\"]").toString());
        }
    }

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void recieveReleaseNotification(@RequestBody ReleasePayload request) {
        logger.debug("Release Webhook: {}",LoggingUtils.logPayload(request));
        try {
            if(Action.PUBLISHED.equalsString(request.getAction())) {
                githubUtils.notifyRelease(request);
            }
        }
        catch (EmailException e) {
            logger.info(ErrorMessage.EMAIL_EXCEPTION.toString());
        }
    }


}
