package com.bot.mrgerkins.builder;

import com.bot.mrgerkins.adapter.github.resource.GithubRelease;
import com.bot.mrgerkins.adapter.github.resource.GithubRepo;
import com.bot.mrgerkins.adapter.github.resource.ReleasePayload;


/**
 * Created by biphilip on 7/17/16.
 */
public class ReleasePayloadBuilder {

    private ReleasePayload releasePayload;

    public ReleasePayloadBuilder() {
        releasePayload = new ReleasePayload();
    }

    public ReleasePayloadBuilder action(String action) {
        releasePayload.setAction(action);
        return this;
    }

    public ReleasePayloadBuilder release(GithubRelease release) {
        releasePayload.setRelease(release);
        return this;
    }

    public ReleasePayloadBuilder repository(GithubRepo repository) {
        releasePayload.setRepository(repository);
        return this;
    }

    public ReleasePayload build() {
        return releasePayload;
    }

}
