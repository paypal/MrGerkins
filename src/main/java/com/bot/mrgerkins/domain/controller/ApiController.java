package com.bot.mrgerkins.domain.controller;

import com.bot.mrgerkins.adapter.api.TagDLNotifyRequest;
import com.bot.mrgerkins.adapter.jenkins.JenkinsClientWrapper;
import com.bot.mrgerkins.adapter.api.ReleaseNotesRequest;
import com.bot.mrgerkins.domain.constants.ErrorMessage;
import com.bot.mrgerkins.domain.utils.GithubUtils;
import com.bot.mrgerkins.domain.utils.LoggingUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * Created by bijilap on 5/26/16.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GithubUtils githubUtils;

    @Autowired
    private JenkinsClientWrapper jenkinsClientWrapper;

    @RequestMapping(value = "/release-notes/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createReleaseNotes(@RequestBody ReleaseNotesRequest request){
        logger.debug(LoggingUtils.logPayload(request));
        try {
            githubUtils.generateReleaseNotes(request.getRepositoryFullName(), request.getBranch(), request.getReleaseVersion());
        }
        catch (HttpException e) {
            logger.info(ErrorMessage.HTTP_EXCEPTION.toString());
        }
        catch (IOException e) {
            logger.info(ErrorMessage.HTTP_IO_EXCEPTION.toString());
        }
    }

    @RequestMapping(value = "/tag/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createTag(@RequestBody ReleaseNotesRequest request){
        logger.debug(LoggingUtils.logPayload(request));
        try {
            githubUtils.generateReleaseNotes(request.getRepositoryFullName(), request.getBranch(), request.getReleaseVersion());
            logger.info("Release notes created");
            jenkinsClientWrapper.kickOffTagBuild(request);
        }
        catch (HttpException e) {
            logger.info(ErrorMessage.HTTP_EXCEPTION.toString());
        }
        catch (IOException e) {
            logger.info(ErrorMessage.HTTP_IO_EXCEPTION.toString());
        }
    }


    /**
     *  Notify authors of PRs which have been included in the tag
     * @param request
     */
    @RequestMapping(value = "/tag/notify-authors", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void notifyTagPRAuthors(@RequestBody ReleaseNotesRequest request){
        logger.debug(LoggingUtils.logPayload(request));
        try {
            githubUtils.notifyTagPRAuthors(request.getRepositoryFullName(), request.getBranch(), request.getReleaseVersion(), request.getBranch());
        }
        catch (HttpException e) {
            logger.info(ErrorMessage.HTTP_EXCEPTION.toString());
        }
        catch (IOException e) {
            logger.info(ErrorMessage.HTTP_IO_EXCEPTION.toString());
        }
    }

    @RequestMapping(value = "/tag/notify-dl", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void notifyTagCreationDL(@RequestBody TagDLNotifyRequest request){
        logger.debug(LoggingUtils.logPayload(request));
        try {
            githubUtils.notifyTagCreationDL(request.getRepositoryFullName(), request.getReleaseVersion(), request.getBuildId(), request.getNotifyDl());
        }
        catch (HttpException e) {
            logger.info(ErrorMessage.HTTP_EXCEPTION.toString());
        }
        catch (IOException e) {
            logger.info(ErrorMessage.HTTP_IO_EXCEPTION.toString());
        }
    }
}
